package com.ptb.pay.api.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.ptb.account.api.IAccountApi;
import com.ptb.account.vo.PtbAccountVo;
import com.ptb.account.vo.param.AccountPayParam;
import com.ptb.account.vo.param.AccountRefundParam;
import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.PlatformEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IOrderApi;
import com.ptb.pay.enums.ErrorCode;
import com.ptb.pay.enums.OrderActionEnum;
import com.ptb.pay.mapper.impl.OrderMapper;
import com.ptb.pay.mapper.impl.ProductMapper;
import com.ptb.pay.model.Order;
import com.ptb.pay.model.Product;
import com.ptb.pay.service.impl.OrderDetailServiceImpl;
import com.ptb.pay.service.interfaces.IOrderService;
import com.ptb.pay.vo.orderdetail.OrderDetailVO;
import com.ptb.pay.vo.product.ProductVO;
import com.ptb.utils.encrypt.SignUtil;
import com.ptb.utils.service.ReturnUtil;
import com.ptb.utils.tool.GenerateOrderNoUtil;
import com.ptb.utils.tool.ParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zuokui.fu on 2016/11/16.
 */
@Service("orderApi")
public class OrderApiImpl implements IOrderApi {

    private static final Logger logger = LoggerFactory.getLogger( OrderApiImpl.class);

    @Autowired
    private IAccountApi accountApi;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderDetailServiceImpl orderDetailService;

    @Transactional( rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ResponseVo<Map<String,Object>> cancelApplyRefund(Long buyerId, Long orderId) throws Exception {
        logger.info( "卖家取消申请退款。buyerId:{} orderId:{}", buyerId, orderId);
        try {
            //参数校验
            if (!ParamUtil.checkParams(buyerId, orderId)) {
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1001.getCode(), ErrorCode.PAY_API_COMMMON_1001.getMessage());
            }
            //检查订单状态
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (null!= order && order.getBuyerId().longValue() != buyerId.longValue()) {
                //买家ID与订单中的买家ID不符
                return ReturnUtil.error(ErrorCode.ORDER_API_5004.getCode(), ErrorCode.ORDER_API_5004.getMessage());
            }
            if (!orderService.checkOrderStatus(OrderActionEnum.BUYER_CANCEL_REFUND, order.getOrderStatus(), order.getSellerStatus(), order.getBuyerStatus())) {
                //订单状态有误
                return ReturnUtil.error(ErrorCode.ORDER_API_5002.getCode(), ErrorCode.ORDER_API_5002.getMessage());
            }
            //更新订单状态、新增订单日志记录
            orderService.updateStatusForCancelRefund( orderId, buyerId, order.getOrderNo());
            Order resultOrder = orderMapper.selectByPrimaryKey( orderId);
            return ReturnUtil.success( orderService.getBuyerOrderStatus( resultOrder.getOrderNo()+resultOrder.getSellerStatus()+resultOrder.getBuyerStatus()));
        } catch ( Exception e){
            logger.error( "买家取消申请退款接口调用失败。error message: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional( rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ResponseVo<Map<String,Object>> agreeRefund(Long salerId, Long orderId, Long money, String deviceType) throws Exception {
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
            if (null!= order && order.getSellerId().longValue() != salerId.longValue()) {
                //卖家ID与订单中的卖家ID不符
                return ReturnUtil.error(ErrorCode.ORDER_API_5001.getCode(), ErrorCode.ORDER_API_5001.getMessage());
            }
            if (!orderService.checkOrderStatus(OrderActionEnum.SALER_AGREE_REFUND, order.getOrderStatus(), order.getSellerStatus(), order.getBuyerStatus())) {
                //订单状态有误
                return ReturnUtil.error(ErrorCode.ORDER_API_5002.getCode(), ErrorCode.ORDER_API_5002.getMessage());
            }
            //检查订单应付款金额
            if (money.longValue() != order.getPayablePrice()) {
                //退款金额有误
                return ReturnUtil.error(ErrorCode.ORDER_API_5003.getCode(), ErrorCode.ORDER_API_5003.getMessage());
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

    @Transactional( rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ResponseVo buyerPayment(Long userId, Long orderId, String plyPassword,String deviceType) throws Exception {
        logger.info( "买家开始付款。userId:{} orderId:{}, plyPassword:{}", userId, orderId,plyPassword);
        try{
            //参数校验
            if (!ParamUtil.checkParams(userId,orderId,plyPassword)){
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1001.getCode(), ErrorCode.PAY_API_COMMMON_1001.getMessage());
            }
            //查询订单信息
            Order order = orderMapper.selectByPrimaryKey(orderId);
            //检查订单是否有误
            if (null!= order && order.getBuyerId().longValue() != userId.longValue()){
                return ReturnUtil.error(ErrorCode.ORDER_API_5001.getCode(), ErrorCode.ORDER_API_5001.getMessage());
            }
            if (!orderService.checkOrderStatus(OrderActionEnum.BUYER_PAY, order.getOrderStatus(), order.getSellerStatus(), order.getBuyerStatus())) {
                //订单状态有误
                return ReturnUtil.error(ErrorCode.ORDER_API_5002.getCode(), ErrorCode.ORDER_API_5002.getMessage());
            }
            //更新订单状态并增加订单日志
            orderService.updateStatusBuyerPayment(order.getPtbOrderId(),userId,order.getOrderNo());

            //调用dubbo付款消费虚拟币
            AccountPayParam param = new AccountPayParam();
            param.setBuyerId(order.getBuyerId());
            param.setSalerId(order.getSellerId());
            param.setMoney(order.getPayablePrice());
            param.setOrderNo(order.getOrderNo());
            param.setPayPassword(plyPassword);
            param.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(deviceType));
            param.setPlatform(PlatformEnum.xiaomi);
            //隐式加密
            TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(param), TreeMap.class);
            String sign = SignUtil.getSignKey(toSign);
            RpcContext.getContext().setAttachment("key", sign);
            ResponseVo<PtbAccountVo> responseVo = accountApi.pay(param);
            if ( !"0".equals( responseVo.getCode())){
                logger.error( "虚拟账户付款dubbo接口调用失败。salerId:{}", userId);
                throw new Exception();
            }
            Order resultOrder = orderMapper.selectByPrimaryKey(orderId);
            return ReturnUtil.success( orderService.getBuyerOrderStatus( resultOrder.getOrderNo()+resultOrder.getSellerStatus()+resultOrder.getBuyerStatus()));
        }catch (Exception e){
            logger.error( "买家付款接口调用失败。error message: {}", e.getMessage());
            throw e;
        }
    }
    @Transactional( rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ResponseVo refund(Long userId, Long orderId, String deviceType) throws Exception {
        logger.info( "买家申请退款。userId:{} orderId:{}", userId, orderId);
        try {
            //参数校验
            if (!ParamUtil.checkParams(userId,orderId)){
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1001.getCode(), ErrorCode.PAY_API_COMMMON_1001.getMessage());
            }
            //检查订单状态
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (null!= order && order.getBuyerId().longValue() != userId.longValue()) {
                //买家ID与订单中的买家ID不符
                return ReturnUtil.error(ErrorCode.ORDER_API_5004.getCode(), ErrorCode.ORDER_API_5004.getMessage());
            }
            if (!orderService.checkOrderStatus(OrderActionEnum.BUYER_APPLY_REFUND, order.getOrderStatus(), order.getSellerStatus(), order.getBuyerStatus())) {
                //订单状态有误
                return ReturnUtil.error(ErrorCode.ORDER_API_5002.getCode(), ErrorCode.ORDER_API_5002.getMessage());
            }
            //更新订单状态、并新增订单日志记录
            orderService.updateStaterefund(orderId,userId, order.getOrderNo());
            Order resultOrder = orderMapper.selectByPrimaryKey(orderId);
            return ReturnUtil.success( orderService.getBuyerOrderStatus( resultOrder.getOrderNo()+resultOrder.getSellerStatus()+resultOrder.getBuyerStatus()));
        }catch (Exception e){
            logger.error( "买家申请退款接口调用失败。error message: {}", e.getMessage());
            throw e;
        }


    }

    @Override
    public ResponseVo submitOrder(long userId, ProductVO productVO) {
        logger.info("买家提交订单 userID:{}", userId);

        try {
            List<Product> products = productMapper.selectByOwnerId(userId);
            if (products == null || products.size() == 0) {
                return ReturnUtil.error("", "no product");
            }
            String orderNo = GenerateOrderNoUtil.createOrderNo(2, productVO.getDeviceType(), 3);
            if (orderNo == null){
                logger.error("generate order no error! userId:{} productId:{}", userId, productVO.getProductId());
                return ReturnUtil.error("", "no product");
            }
            orderService.insertNewOrder(userId, productVO.getOwnerId(), productVO.getPrice(), orderNo);

            OrderDetailVO orderDetailVO = orderDetailService.convertOrderDetailVO(orderNo, productVO.getPrice(), 0, productVO.getProductId());
            int insert = orderDetailService.insertOrderDetail(orderDetailVO);
            if (insert < 1){
                logger.error("insert order detail error! orderNo:{} product iD:{}", orderNo, productVO.getProductId());
                throw new Exception("order detail error!");
            }

        }catch (Exception e){
            logger.error("submit order error!", e);
            return ReturnUtil.error("", "no product");
        }
        return null;
    }
}
