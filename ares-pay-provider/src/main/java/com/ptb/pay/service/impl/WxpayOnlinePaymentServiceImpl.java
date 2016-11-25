package com.ptb.pay.service.impl;

import com.ptb.pay.service.interfaces.IOnlinePaymentService;
import com.ptb.pay.vo.CheckPayResultVO;

import java.util.Map;

/**
 * Description: 微信支付相关接口
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:12  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class WxpayOnlinePaymentServiceImpl implements IOnlinePaymentService{

    @Override
    public String getPaymentInfo(String rechargeOrderNo, Long price) throws Exception{
        return null;
    }

    @Override
    public String getPcPaymentInfo(String rechargeOrderNo, Long price) throws Exception {
        return null;
    }

    @Override
    public CheckPayResultVO checkPayResult(String payResult) throws Exception {
        return null;
    }

    @Override
    public boolean notifyPayResult(Map<String, String> params) throws Exception {
        return false;
    }
}
