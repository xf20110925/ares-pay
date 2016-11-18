package com.ptb.pay.service.interfaces;

import com.ptb.pay.enums.OrderActionEnum;

import java.util.Map;

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

    /**
     * 买家取消申请退款更新订单状态
     * @param ptbOrderId 订单主键
     * @param buyerId 买家用户编号
     * @param orderNo 订单编号
     * @throws Exception
     */
    void updateStatusForCancelRefund( Long ptbOrderId, Long buyerId, String orderNo) throws Exception;

    /**
     * 根据订单状态获取客户端展示的卖家按钮及文案
     * @param multiOrderStatus 订单状态+卖家状态+买家状态 eg: 买家已付款，订单状态、卖家状态、买家状态分别为：1、0、1，则参数为：101
     * @return
     */
    public Map<String, Object> getSalerOrderStatus(String multiOrderStatus);

    /**
     * 根据订单状态获取客户端展示的买家按钮及文案
     * @param multiOrderStatus 订单状态+卖家状态+买家状态 eg: 买家已付款，订单状态、卖家状态、买家状态分别为：1、0、1，则参数为：101
     * @return
     */
    public Map<String, Object> getBuyerOrderStatus( String multiOrderStatus);

    /**
     * 买家付款更新订单状态
     * @param userId
     * @param orderNo
     */
    void updateStatusBuyerPayment(Long ptbOrderId,Long userId,String orderNo) throws Exception;


     /**
     * 买家申请退款
     * @param ptbOrderId
     * @param userId
     * @param orderNo
     * @throws Exception
     */
    void updateStaterefund(Long ptbOrderId,Long userId,String orderNo)throws Exception;



}
