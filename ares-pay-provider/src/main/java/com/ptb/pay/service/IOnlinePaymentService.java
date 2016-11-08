package com.ptb.pay.service;

/**
 * Description:
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:09  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public interface IOnlinePaymentService {

    /**
     * Description: 获取第三方支付sdk所需的参数
     * All Rights Reserved.
     * @param
     * @return
     * @version 1.0  2016-11-08 13:10 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    String getPaymentInfo(String rechargeOrderNo, Long price);
}
