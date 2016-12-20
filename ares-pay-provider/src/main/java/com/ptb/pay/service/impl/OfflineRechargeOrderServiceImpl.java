package com.ptb.pay.service.impl;

import com.ptb.common.enums.RechargeOrderInvoiceStatusEnum;
import com.ptb.common.enums.RechargeOrderStatusEnum;
import com.ptb.pay.conf.payment.OfflinePaymentConfig;
import com.ptb.pay.enums.RechargeOrderLogActionTypeEnum;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.service.interfaces.IPaymentService;
import com.ptb.pay.service.interfaces.IRechargeOrderLogService;
import com.ptb.pay.service.interfaces.IRechargeOrderService;
import com.ptb.pay.vo.recharge.RechargeOrderParamsVO;
import com.ptb.utils.tool.GenerateOrderNoUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 线下充值接口
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-07 19:45  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Component
@Transactional
public class OfflineRechargeOrderServiceImpl implements IRechargeOrderService {

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IRechargeOrderLogService rechargeOrderLogService;

    @Override
    public RechargeOrder createRechargeOrder(RechargeOrderParamsVO paramsVO) throws Exception {

        String rechargeOrderNo = GenerateOrderNoUtil.createRechargeOrderNo(paramsVO.getDeviceType(), paramsVO.getPayMethod());
        if(StringUtils.isBlank(rechargeOrderNo)){
            return null;
        }
        Date now = new Date();
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setCreateTime(now);
        rechargeOrder.setOrderNo(paramsVO.getOrderNo());
        rechargeOrder.setPayMethod(paramsVO.getPayMethod());
        rechargeOrder.setPayType(0); //银行汇款
        rechargeOrder.setRechargeOrderNo(rechargeOrderNo);
        rechargeOrder.setStatus(RechargeOrderStatusEnum.review.getRechargeOrderStatus());
        rechargeOrder.setTotalAmount(paramsVO.getRechargeAmount());
        rechargeOrder.setVerificationCode(RandomStringUtils.randomNumeric(6));
        rechargeOrder.setUserId(paramsVO.getUserId());
        rechargeOrder.setInvoiceStatus(RechargeOrderInvoiceStatusEnum.noopen.getRechargeOrderInvoiceStatus());
        rechargeOrder.setDeviceType(paramsVO.getDeviceType());
        rechargeOrderMapper.insert(rechargeOrder);

        rechargeOrderLogService.saveUserOpLog(rechargeOrder.getRechargeOrderNo(),
                RechargeOrderLogActionTypeEnum.CREATED.getActionType(), null, rechargeOrder.getUserId());
        return rechargeOrder;
    }

    @Override
    public Map<String, Object> getReturnData(RechargeOrder rechargeOrder) throws Exception {

        Map<String, Object> returnData = new HashMap<String, Object>();
        returnData.put("rechargeOrderNo", rechargeOrder.getRechargeOrderNo());
        returnData.put("verificationCode", rechargeOrder.getVerificationCode());
        returnData.put("rechargeAmount", rechargeOrder.getTotalAmount());
        returnData.put("payMethod", rechargeOrder.getPayMethod());
        OfflinePaymentConfig offlinePaymentConfig = paymentService.getOfflinePaymentConfig();
        if(offlinePaymentConfig != null){
            returnData.put("bankName", offlinePaymentConfig.getBankName());
            returnData.put("openAccountBankName", offlinePaymentConfig.getOpenAccountBankName());
            returnData.put("openAccountUserName", offlinePaymentConfig.getOpenAccountUserName());
            returnData.put("openAccountUserNum", offlinePaymentConfig.getOpenAccountUserNum());
        }
        return returnData;
    }



}
