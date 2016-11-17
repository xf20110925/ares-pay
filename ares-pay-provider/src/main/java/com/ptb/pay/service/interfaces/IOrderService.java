package com.ptb.pay.service.interfaces;

import com.ptb.pay.enums.OrderActionEnum;

/**
 * Created by zuokui.fu on 2016/11/16.
 */
public interface IOrderService {

    /**
     * 检查卖家状态
     * @param orderAction 订单操作标志
     * @param orderStatus 订单状态
     * @param salerStatus 卖家状态
     * @param buyerStatus 买家状态
     * @return
     */
    public boolean checkOrderStatus(OrderActionEnum orderAction, int orderStatus, int salerStatus, int buyerStatus);

    /**
     * 卖家同意退款更新订单状态
     * @param ptbOrderId 订单主键
     * @param salerId 卖家用户编号
     * @param orderNo 订单编号
     */
    void updateStatusForArgeeRefund( Long ptbOrderId, Long salerId, String orderNo) throws Exception;

}
