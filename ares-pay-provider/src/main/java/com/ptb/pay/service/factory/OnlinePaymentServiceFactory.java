package com.ptb.pay.service.factory;

import com.ptb.common.enums.OnlinePaymentTypeEnum;
import com.ptb.pay.service.IOnlinePaymentService;
import com.ptb.pay.service.impl.AlipayOnlinePaymentServiceImpl;
import com.ptb.pay.service.impl.WxpayOnlinePaymentServiceImpl;

/**
 * Description: 在线支付方式工厂
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:13  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class OnlinePaymentServiceFactory {

    public static IOnlinePaymentService createService(int payType){
        if (OnlinePaymentTypeEnum.ALIPAY.getPaymentTypeId() == payType)
            return new AlipayOnlinePaymentServiceImpl();
        else if (OnlinePaymentTypeEnum.WXPAY.getPaymentTypeId() == payType)
            return new WxpayOnlinePaymentServiceImpl();
        else
            return null;
    }
}
