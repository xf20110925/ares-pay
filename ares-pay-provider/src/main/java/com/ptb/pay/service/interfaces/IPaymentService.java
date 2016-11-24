package com.ptb.pay.service.interfaces;

import com.ptb.pay.conf.payment.OfflinePaymentConfig;

/**
 * Created by zuokui.fu on 2016/11/23.
 */
public interface IPaymentService {

    /**
     * 获取线下支付信息
     * @return
     */
    public OfflinePaymentConfig getOfflinePaymentConfig();
}
