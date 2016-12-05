package com.ptb.pay.api.impl;

import com.github.pagehelper.PageHelper;
import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.PaymentMethodEnum;
import com.ptb.common.enums.RechargeOrderStatusEnum;
import com.ptb.common.errorcode.CommonErrorCode;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IRechargeOrderApi;
import com.ptb.pay.conf.payment.OfflinePaymentConfig;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.model.RechargeOrderExample;
import com.ptb.pay.service.factory.RechargeOrderServiceFactory;
import com.ptb.pay.service.interfaces.IPaymentService;
import com.ptb.pay.service.interfaces.IRechargeOrderService;
import com.ptb.pay.vo.recharge.RechargeOrderParamsVO;
import com.ptb.pay.vo.recharge.RechargeOrderQueryVO;
import com.ptb.pay.vo.recharge.RechargeOrderVO;
import com.ptb.service.api.IBaiduPushApi;
import com.ptb.utils.db.Page;
import com.ptb.utils.service.ReturnUtil;
import com.ptb.utils.tool.ChangeMoneyUtil;
import enums.MessageTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import vo.param.PushMessageParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 充值订单API实现
 * All Rights Reserved.
 * @version 1.0  2016-11-15 10:35 by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Component("rechargeOrderApi")
public class RechargeOrderApiImpl implements IRechargeOrderApi {

    private static Logger logger = LoggerFactory.getLogger( RechargeOrderApiImpl.class);

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;
    @Autowired
    private IPaymentService paymentService;
    @Autowired
    private IBaiduPushApi baiduPushApi;

    @Override
    public ResponseVo<Map<String, Object>> createRechargeOrder(RechargeOrderParamsVO paramsVO) throws Exception {
        IRechargeOrderService rechargeOrderService = RechargeOrderServiceFactory.createService(paramsVO.getPayMethod());
        RechargeOrder rechargeOrder = rechargeOrderService.createRechargeOrder(paramsVO);
        if(rechargeOrder == null){
            return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_ARGSERROR.getCode(),
                    CommonErrorCode.COMMMON_ERROR_ARGSERROR.getMessage());
        }
        if ( PaymentMethodEnum.offline.getPaymentMethod() == paramsVO.getPayMethod()){
            //推送消息
            try {
                //推送消息
                PushMessageParam param = new PushMessageParam();
                param.setUserId(paramsVO.getUserId());
                param.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(paramsVO.getDeviceType()));
                param.setTitle("审核中（线下打款）");
                param.setMessage("提交成功，充值金额" + ChangeMoneyUtil.fromFenToYuan(rechargeOrder.getTotalAmount()) + "元，请尽快给平台账户进行打款，便于系统审核");
                param.setMessageType(MessageTypeEnum.OFFLINE_RECHARGE.getMessageType());
                Map<String, Object> keyMap = new HashMap<>();
                keyMap.put("id", rechargeOrder.getPtbRechargeOrderId());
                param.setContentParam( keyMap);
                baiduPushApi.pushMessage(param);
            }catch (Exception e){
                logger.error( "线下打款消息推送失败。errorMsg:{}", e.getMessage());
            }
        }
        return ReturnUtil.success(rechargeOrderService.getReturnData(rechargeOrder));
    }

    @Override
    public ResponseVo<List<RechargeOrderVO>> getRechargeOrderList(Long userId) throws Exception {
        return getRechargeOrderList(userId, 0, 10000);
    }

    @Override
    public ResponseVo<List<RechargeOrderVO>> getRechargeOrderList(Long userId, int start, int end) throws Exception {
        if(userId == null){
            return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_ARGSERROR.getCode(),
                    CommonErrorCode.COMMMON_ERROR_ARGSERROR.getMessage());
        }

        RechargeOrderExample example = new RechargeOrderExample();

        List<Integer> status = new ArrayList<Integer>();
        status.add(RechargeOrderStatusEnum.paid.getRechargeOrderStatus());
        status.add(RechargeOrderStatusEnum.review.getRechargeOrderStatus());
        example.createCriteria().andUserIdEqualTo(userId).andStatusIn(status);
        example.setOrderByClause("create_time desc");
        Page page = new Page();
        page.setLimit(start);
        page.setLimit(end - start);
        example.setPage(page);
        List<RechargeOrder> orders = rechargeOrderMapper.selectByExample(example);
        List<RechargeOrderVO> returnData = new ArrayList<RechargeOrderVO>();
        if(CollectionUtils.isEmpty(orders)){
            return ReturnUtil.success(returnData);
        }
        for (RechargeOrder order : orders) {
            RechargeOrderVO orderVO = new RechargeOrderVO();
            orderVO.setPayTime(order.getPayTime());
            orderVO.setPayType(order.getPayType());
            orderVO.setCreateTime(order.getCreateTime());
            orderVO.setUserId(order.getUserId());
            orderVO.setDeviceType(order.getDeviceType());
            orderVO.setOrderId(order.getPtbRechargeOrderId());
            orderVO.setRechargeAmount(order.getTotalAmount());
            orderVO.setStatus(order.getStatus());
            orderVO.setVerificationCode(order.getVerificationCode());
            orderVO.setPayMethod(order.getPayMethod());
            orderVO.setRechargeOrderNo(order.getRechargeOrderNo());
            orderVO.setPtbRechargeOrderId( order.getPtbRechargeOrderId());
            returnData.add(orderVO);
        }
        return ReturnUtil.success(returnData);
    }

    @Override
    public ResponseVo<List<RechargeOrderVO>> getRechargeOrderListByPage(int pageNum, int pageSize, RechargeOrderQueryVO rechargeOrderQueryVO) throws Exception {
        RechargeOrderExample example = new RechargeOrderExample();

        RechargeOrderExample.Criteria c = example.createCriteria();
        //充值订单号
        if(StringUtils.isNotBlank(rechargeOrderQueryVO.getRechargeOrderNo())){
            c.andRechargeOrderNoEqualTo(rechargeOrderQueryVO.getRechargeOrderNo());
        }
        //验证码
        if(StringUtils.isNotBlank(rechargeOrderQueryVO.getVerificationCode())){
            c.andVerificationCodeEqualTo(rechargeOrderQueryVO.getVerificationCode());
        }
        //充值类型
        if(rechargeOrderQueryVO.getPayMethod() != null){
            c.andPayMethodEqualTo(rechargeOrderQueryVO.getPayMethod());
        }
        //充值状态
        if(rechargeOrderQueryVO.getStatus() != null){
            c.andStatusEqualTo(rechargeOrderQueryVO.getStatus());
        }
        //发票状态
        if(rechargeOrderQueryVO.getInvoiceStatus() != null){
            c.andInvoiceStatusEqualTo(rechargeOrderQueryVO.getInvoiceStatus());
        }
        //发票ID
        if(rechargeOrderQueryVO.getInvoiceId() != null){
            c.andInvoiceIdEqualTo(rechargeOrderQueryVO.getInvoiceId());
        }
        //创建时间
        if(rechargeOrderQueryVO.getStartTime() != null){
            c.andCreateTimeGreaterThanOrEqualTo(rechargeOrderQueryVO.getStartTime());
        }
        //创建时间
        if(rechargeOrderQueryVO.getEndTime() != null){
            c.andCreateTimeLessThanOrEqualTo(rechargeOrderQueryVO.getEndTime());
        }

        example.setOrderByClause("create_time desc");

        PageHelper.startPage(pageNum, pageSize); //开启分页查询，通过拦截器实现，紧接着执行的sql会被拦截
        List<RechargeOrder> orders = rechargeOrderMapper.selectByExample(example);
        List<RechargeOrderVO> returnData = new ArrayList<RechargeOrderVO>();
        if(CollectionUtils.isEmpty(orders)){
            return ReturnUtil.success(returnData);
        }
        for (RechargeOrder order : orders) {
            RechargeOrderVO orderVO = new RechargeOrderVO();
            orderVO.setPayTime(order.getPayTime());
            orderVO.setPayType(order.getPayType());
            orderVO.setCreateTime(order.getCreateTime());
            orderVO.setUserId(order.getUserId());
            orderVO.setDeviceType(order.getDeviceType());
            orderVO.setOrderId(order.getPtbRechargeOrderId());
            orderVO.setRechargeAmount(order.getTotalAmount());
            orderVO.setStatus(order.getStatus());
            orderVO.setVerificationCode(order.getVerificationCode());
            orderVO.setPayMethod(order.getPayMethod());
            orderVO.setRechargeOrderNo(order.getRechargeOrderNo());
            orderVO.setPtbRechargeOrderId( order.getPtbRechargeOrderId());
            orderVO.setInvoiceId(order.getInvoiceId());
            returnData.add(orderVO);
        }
        return ReturnUtil.success(returnData);
    }

    @Override
    public ResponseVo<RechargeOrderVO> getRechargeOrderDetail(Long rechargeOrderId, String rechargeOrderNo, Long userId) {
        RechargeOrderVO orderVO = new RechargeOrderVO();
        RechargeOrder order = rechargeOrderMapper.selectOne( rechargeOrderId, rechargeOrderNo, userId);
        if ( null == order) {
            return ReturnUtil.success( null);
        }
        orderVO.setPayTime(order.getPayTime());
        orderVO.setPayType(order.getPayType());
        orderVO.setCreateTime(order.getCreateTime());
        orderVO.setUserId(order.getUserId());
        orderVO.setDeviceType(order.getDeviceType());
        orderVO.setOrderId(order.getPtbRechargeOrderId());
        orderVO.setRechargeAmount(order.getTotalAmount());
        orderVO.setStatus(order.getStatus());
        orderVO.setVerificationCode(order.getVerificationCode());
        orderVO.setPayMethod(order.getPayMethod());
        orderVO.setRechargeOrderNo(order.getRechargeOrderNo());
        orderVO.setPtbRechargeOrderId(order.getPtbRechargeOrderId());
        //线下充值展示收款行信息
        if ( 2 == order.getPayMethod().intValue()) {
            OfflinePaymentConfig config = paymentService.getOfflinePaymentConfig();
            Map<String, Object> bankInfo = new HashMap<>();
            bankInfo.put("bankName", config.getBankName());
            bankInfo.put("openAccountBankName", config.getOpenAccountBankName());
            bankInfo.put("openAccountUserName", config.getOpenAccountUserName());
            bankInfo.put("openAccountUserNum", config.getOpenAccountUserNum());
            orderVO.setBankInfo(bankInfo);
        }
        return ReturnUtil.success( orderVO);
    }
}
