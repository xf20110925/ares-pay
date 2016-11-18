package com.ptb.pay.api.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.ptb.account.api.IAccountApi;
import com.ptb.account.vo.PtbAccountVo;
import com.ptb.account.vo.param.AccountRefundParam;
import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.PlatformEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IOrderApi;
import com.ptb.pay.enums.ErrorCode;
import com.ptb.pay.enums.OrderActionEnum;
import com.ptb.pay.mapper.impl.OrderMapper;
import com.ptb.pay.model.Order;
import com.ptb.pay.service.interfaces.IOrderService;
import com.ptb.utils.encrypt.SignUtil;
import com.ptb.utils.service.ReturnUtil;
import com.ptb.utils.tool.ParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.TreeMap;

/**
 * Created by zuokui.fu on 2016/11/16.
 */
public class OrderApiImpl implements IOrderApi {

    private static final Logger logger = LoggerFactory.getLogger( OrderApiImpl.class);

    @Autowired
    private IAccountApi accountApi;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IOrderService orderService;

    @Override
    public ResponseVo cancelApplyRefund(Long salerId) {
        return null;
    }

    @Transactional( rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ResponseVo agreeRefund( Long salerId, Long orderId, Long money, String deviceType) throws Exception {
        logger.info( "卖家同意退款。salerId:{} orderId:{}, money:{}, deviceType:{}", salerId, orderId, money, deviceType);
        try {
            //参数校验
            if (!ParamUtil.checkParams(salerId, orderId, money, deviceType)) {
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1001.getCode(), ErrorCode.PAY_API_COMMMON_1001.getMessage());
            }
            if (money <= 0) {
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1002.getCode(), ErrorCode.PAY_API_COMMMON_1002.getMessage());
            }
            //检查订单状态
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (order.getSellerId().longValue() != salerId.longValue()) {
                //TODO 卖家ID有误
            }
            if (!orderService.checkOrderStatus(OrderActionEnum.SALER_AGREE_REFUND, order.getOrderStatus(), order.getSellerStatus(), order.getBuyerStatus())) {
                //TODO 订单状态有误
            }
            //检查订单应付款金额
            if (money.longValue() != order.getPayablePrice()) {
                //TODO 退款金额有误
            }
            //更新订单状态、新增订单日志记录
            orderService.updateStatusForArgeeRefund( orderId, salerId, order.getOrderNo());
            //调用虚拟账户退款接口
            AccountRefundParam param = new AccountRefundParam();
            param.setBuyerId(order.getBuyerId());
            param.setSalerId(order.getSellerId());
            param.setMoney(money);
            param.setOrderNo( order.getOrderNo());
            param.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(deviceType));
            param.setPlatform(PlatformEnum.xiaomi);
            //隐式加密
            TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(param), TreeMap.class);
            String sign = SignUtil.getSignKey(toSign);
            RpcContext.getContext().setAttachment("key", sign);
            ResponseVo<PtbAccountVo> accountResponse = accountApi.refund(param);
            if ( !"0".equals( accountResponse.getCode())){
                logger.error( "虚拟账户退款dubbo接口调用失败。salerId:{}", salerId);
                throw new Exception();
            }
            Order resultOrder = orderMapper.selectByPrimaryKey( orderId);
            return ReturnUtil.success( orderService.getSalerOrderStatus( resultOrder.getOrderNo()+resultOrder.getSellerStatus()+resultOrder.getBuyerStatus()));
        } catch ( Exception e){
            logger.error( "卖家同意退款接口调用失败。error message: {}", e.getMessage());
            throw e;
        }
    }
}
