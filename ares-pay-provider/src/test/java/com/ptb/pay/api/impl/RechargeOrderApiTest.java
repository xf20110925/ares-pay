package com.ptb.pay.api.impl;

import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.OnlinePaymentTypeEnum;
import com.ptb.common.enums.PaymentMethodEnum;
import com.ptb.pay.BaseTest;
import com.ptb.pay.api.IRechargeOrderApi;
import com.ptb.pay.vo.recharge.RechargeOrderParamsVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        p.setDeviceType(DeviceTypeEnum.android.getDeviceType());
        p.setPayMethod(PaymentMethodEnum.online.getPaymentMethod());
        p.setPayType(OnlinePaymentTypeEnum.YLPAY.getPaymentTypeId());
        p.setRechargeAmount( 1000L);
        try {
            rechargeOrderApi.createRechargeOrder( p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void batchUpdatePayFee(){
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> m1 = new HashMap<>();
        m1.put("processingAmount", 100);
        m1.put("rechargeOrderNo", "CZAS1611082043000022");
        list.add( m1);

        Map<String, Object> m2 = new HashMap<>();
        m2.put("processingAmount", 200);
        m2.put("rechargeOrderNo", "CZAS1611082042000021");
        list.add( m2);
        rechargeOrderApi.batchUpdatePayFee(list);
    }


}
