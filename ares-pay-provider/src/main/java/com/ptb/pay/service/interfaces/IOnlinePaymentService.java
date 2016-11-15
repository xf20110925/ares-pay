package com.ptb.pay.service.interfaces;

import vo.CheckPayResultVO;

import java.util.Map;

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

    /**
     * Description: 异步通知支付结果
     * All Rights Reserved.
     * @param
     * @return
     * @version 1.0  2016-11-09 17:36 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    boolean notifyPayResult(Map<String, String> params) throws Exception;
}
