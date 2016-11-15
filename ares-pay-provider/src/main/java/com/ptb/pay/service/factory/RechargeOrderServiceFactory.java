package com.ptb.pay.service.factory;

import com.ptb.common.enums.PaymentMethodEnum;
import com.ptb.pay.service.interfaces.IRechargeOrderService;
import com.ptb.pay.system.SpringContextHolder;

/**
 * Description:
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-07 19:46  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class RechargeOrderServiceFactory {

    /**
     * Description: 创建service
     * All Rights Reserved.
     * @param
     * @return
     * @version 1.0  2016-11-07 19:49 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    public static IRechargeOrderService createService(int payMethod){
        if (PaymentMethodEnum.online.getPaymentMethod() == payMethod)
            return SpringContextHolder.getBean("onlineRechargeOrderServiceImpl");
        else
            return SpringContextHolder.getBean("offlineRechargeOrderServiceImpl");
    }
}
