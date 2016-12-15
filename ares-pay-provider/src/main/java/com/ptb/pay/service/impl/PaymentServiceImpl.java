package com.ptb.pay.service.impl;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.conf.payment.OfflinePaymentConfig;
import com.ptb.pay.service.interfaces.IPaymentService;
import com.ptb.service.api.ISystemConfigApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zuokui.fu on 2016/11/23.
 */
@Service
public class PaymentServiceImpl implements IPaymentService {

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

    @Resource
    private ISystemConfigApi systemConfigApi;


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
