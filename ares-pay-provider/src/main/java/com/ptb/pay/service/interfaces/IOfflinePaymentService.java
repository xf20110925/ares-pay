package com.ptb.pay.service.interfaces;

import com.ptb.pay.model.RechargeOrder;

/**
 * Description: 线下充值业务类
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:09  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public interface IOfflinePaymentService {

    /**
     * Description: 充值
     * All Rights Reserved.
     * @param
     * @return 
     * @version 1.0  2016-12-07 18:49 by wgh（guanhua.wang@pintuibao.cn）创建
     */ 
    boolean recharge(RechargeOrder rechargeOrder, Long adminId) throws Exception;
}
