package com.ptb.pay.service.factory;

import com.ptb.common.enums.OnlinePaymentTypeEnum;
import com.ptb.pay.service.interfaces.IOnlinePaymentService;
import com.ptb.pay.system.SpringContextHolder;

/**
 * Description: 在线支付方式工厂
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:13  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class OnlinePaymentServiceFactory {

    public static IOnlinePaymentService createService(int payType){
        if (OnlinePaymentTypeEnum.ALIPAY.getPaymentTypeId() == payType)
            return SpringContextHolder.getBean("alipayOnlinePaymentServiceImpl");
        else if (OnlinePaymentTypeEnum.WXPAY.getPaymentTypeId() == payType)
            return SpringContextHolder.getBean("wxpayOnlinePaymentServiceImpl");
        else if (OnlinePaymentTypeEnum.YLPAY.getPaymentTypeId() == payType)
            return SpringContextHolder.getBean("unionpayOnlinePaymentServiceImpl");
        else
            return null;
    }
}
