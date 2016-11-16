package com.ptb.pay.api;

import com.ptb.common.vo.ResponseVo;

/**
 * 订单相关的接口服务
 * Created by zuokui.fu on 2016/11/16.
 */
public interface IOrderApi {

    /**
     * 买家取消申请退款
     * @param orderNo 订单编号
     * @return
     */
    ResponseVo cancelApplyRefund( String orderNo);

    /**
     * 卖家同意退款
     * @param orderNo 订单编号
     * @param money 退款金额
     * @param deviceType 设备类型
     * @return
     */
    ResponseVo agreeRefund( String orderNo, Long money, String deviceType);
}
