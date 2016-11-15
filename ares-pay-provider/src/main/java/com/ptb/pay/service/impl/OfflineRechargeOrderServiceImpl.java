package com.ptb.pay.service.impl;

import com.ptb.common.enums.RechargeOrderStatusEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.conf.payment.OfflinePaymentConfig;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.service.interfaces.IRechargeOrderService;
import com.ptb.service.api.ISystemConfigApi;
import com.ptb.utils.tool.GenerateOrderNoUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vo.RechargeOrderParamsVO;

import java.util.*;

/**
 * Description: 线下充值接口
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-07 19:45  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Component
@Transactional
public class OfflineRechargeOrderServiceImpl implements IRechargeOrderService {

    /**
     * 银行名称
     */
    private static final String SYSTEM_CONFIG_BANKNAME = "offline.recharge.bankname";

    /**
     * 开户行
     */
    private static final String SYSTEM_CONFIG_OPENACCOUNT_BANKNAME = "offline.recharge.openAccountBankName";

    /**
     * 开户名称
     */
    private static final String SYSTEM_CONFIG_OPENACCOUNT_USERNAME = "offline.recharge.openAccountUserName";

    /**
     * 开户账号
     */
    private static final String SYSTEM_CONFIG_OPENACCOUNT_USERNUM = "offline.recharge.openAccountUserNum";

    /**
     * 线下充值方式
     */
    private static OfflinePaymentConfig OFFLINE_PAYMENT_CONFIG;

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Autowired
    private ISystemConfigApi systemConfigApi;

    @Override
    public RechargeOrder createRechargeOrder(RechargeOrderParamsVO paramsVO) throws Exception {
        Date now = new Date();
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setCreateTime(now);
        rechargeOrder.setOrderNo(paramsVO.getOrderNo());
        rechargeOrder.setPayMethod(paramsVO.getPayMethod());
        rechargeOrder.setRechargeOrderNo(GenerateOrderNoUtil.createRechargeOrderNo(paramsVO.getDeviceType(), paramsVO.getPayMethod()));
        rechargeOrder.setStatus(RechargeOrderStatusEnum.unpay.getRechargeOrderStatus());
        rechargeOrder.setTotalAmount(paramsVO.getRechargeAmount());
        rechargeOrder.setVerificationCode(RandomStringUtils.randomNumeric(6));
        rechargeOrder.setUserId(paramsVO.getUserId());
        rechargeOrder.setDeviceType(paramsVO.getDeviceType());
        rechargeOrderMapper.insert(rechargeOrder);
        return rechargeOrder;
    }

    @Override
    public Map<String, Object> getReturnData(RechargeOrder rechargeOrder) throws Exception {

        Map<String, Object> returnData = new HashMap<String, Object>();
        returnData.put("rechargeOrderNo", rechargeOrder.getRechargeOrderNo());
        returnData.put("verificationCode", rechargeOrder.getVerificationCode());
        returnData.put("rechargeAmount", rechargeOrder.getTotalAmount());
        returnData.put("payMethod", rechargeOrder.getPayMethod());
        OfflinePaymentConfig offlinePaymentConfig = getOfflinePaymentConfig();
        if(offlinePaymentConfig != null){
            returnData.put("bankName", offlinePaymentConfig.getBankName());
            returnData.put("openAccountBankName", offlinePaymentConfig.getOpenAccountBankName());
            returnData.put("openAccountUserName", offlinePaymentConfig.getOpenAccountUserName());
            returnData.put("openAccountUserNum", offlinePaymentConfig.getOpenAccountUserNum());
        }
        return returnData;
    }

    /**
     * Description: 获取线下充值方式信息
     * All Rights Reserved.
     * @param
     * @return
     * @version 1.0  2016-11-08 11:17 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    public OfflinePaymentConfig getOfflinePaymentConfig() {
        if(OFFLINE_PAYMENT_CONFIG == null){
            OFFLINE_PAYMENT_CONFIG = new OfflinePaymentConfig();
            List<String> params = new ArrayList<String>();
            params.add(SYSTEM_CONFIG_BANKNAME);
            params.add(SYSTEM_CONFIG_OPENACCOUNT_BANKNAME);
            params.add(SYSTEM_CONFIG_OPENACCOUNT_USERNAME);
            params.add(SYSTEM_CONFIG_OPENACCOUNT_USERNUM);
            ResponseVo<Map<String, String>> result = systemConfigApi.getConfigs(params);
            if(result == null || !"0".equals(result.getCode())){
                return null;
            }
            Map<String, String> returnData = result.getData();
            OFFLINE_PAYMENT_CONFIG.setBankName(returnData.get(SYSTEM_CONFIG_BANKNAME));
            OFFLINE_PAYMENT_CONFIG.setOpenAccountBankName(returnData.get(SYSTEM_CONFIG_OPENACCOUNT_BANKNAME));
            OFFLINE_PAYMENT_CONFIG.setOpenAccountUserName(returnData.get(SYSTEM_CONFIG_OPENACCOUNT_USERNAME));
            OFFLINE_PAYMENT_CONFIG.setOpenAccountUserNum(returnData.get(SYSTEM_CONFIG_OPENACCOUNT_USERNUM));
        }
        return  OFFLINE_PAYMENT_CONFIG;
    }

}
