package com.ptb.pay.api.impl;

import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.OnlinePaymentTypeEnum;
import com.ptb.common.enums.PaymentMethodEnum;
import com.ptb.pay.BaseTest;
import com.ptb.pay.api.IRechargeOrderApi;
import com.ptb.pay.vo.recharge.RechargeOrderParamsVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zuokui.fu on 2016/12/6.
 */
public class RechargeOrderApiTest extends BaseTest {

    @Autowired
    private IRechargeOrderApi rechargeOrderApi;

    @Test
    public void createRechargeOrder(){
        RechargeOrderParamsVO p = new RechargeOrderParamsVO();
        p.setUserId( 1156L);
        p.setDeviceType(DeviceTypeEnum.PC.getDeviceType());
        p.setPayMethod(PaymentMethodEnum.online.getPaymentMethod());
        p.setPayType(OnlinePaymentTypeEnum.ALIPAY.getPaymentTypeId());
        p.setRechargeAmount( 1000L);
        try {
            rechargeOrderApi.createRechargeOrder( p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
