package com.ptb.pay.api.impl;

import com.ptb.pay.BaseTest;
import com.ptb.pay.api.IOrderApi;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zuokui.fu on 2016/11/18.
 */
public class OrderApiImplTest extends BaseTest {

    @Autowired
    private IOrderApi orderApi;

    @Test
    public void cancelApplyRefund(){
        try {
            orderApi.cancelApplyRefund( 777L, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void agreeRefund(){
        try {
            orderApi.agreeRefund( 776L, 1L, 800000L, "iphone");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}