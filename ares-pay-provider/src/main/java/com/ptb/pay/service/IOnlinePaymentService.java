package com.ptb.pay.service;

import vo.CheckPayResultVO;

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
    String getPaymentInfo(String rechargeOrderNo, Long price) throws Exception;

    /**
     * Description: 校验线上支付结果
     * All Rights Reserved.
     * @param
     * @return
     * @version 1.0  2016-11-09 10:15 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    CheckPayResultVO checkPayResult(String payResult) throws Exception;
}
