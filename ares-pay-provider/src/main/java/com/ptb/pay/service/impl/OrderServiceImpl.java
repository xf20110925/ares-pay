package com.ptb.pay.service.impl;

import com.ptb.pay.enums.OrderActionEnum;
import com.ptb.pay.mapper.impl.OrderLogMapper;
import com.ptb.pay.mapper.impl.OrderMapper;
import com.ptb.pay.model.Order;
import com.ptb.pay.model.OrderLog;
import com.ptb.pay.service.interfaces.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;

/**
 * Created by zuokui.fu on 2016/11/16.
 */
@Service("orderService")
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderLogMapper orderLogMapper;

    private static Map<String, Object> salerOrderStatusMap = new HashMap<>();
    private static Map<String, Object> buyerOrderStatusMap = new HashMap<>();

    static {
        Map<String, Object> map =  new HashMap();map.put( "button", "6");   map.put( "desc", "等待买家付款");salerOrderStatusMap.put( "000", map);
        Map<String, Object> map1 = new HashMap();map1.put( "button", null);map1.put( "desc", "关闭交易");salerOrderStatusMap.put( "305", map1);
        Map<String, Object> map2 = new HashMap();map2.put( "button", "8"); map2.put( "desc", "进行中");salerOrderStatusMap.put( "101", map2);
        Map<String, Object> map3 = new HashMap();map3.put( "button", "7"); map3.put( "desc", "买家申请退款");salerOrderStatusMap.put( "102", map3);
        Map<String, Object> map4 = new HashMap();map4.put( "button", null);map4.put( "desc", "退款成功");salerOrderStatusMap.put( "322", map4);
        Map<String, Object> map5 = new HashMap();map5.put( "button", null);map5.put( "desc", "等待买家确认完成");salerOrderStatusMap.put( "111", map5);
        Map<String, Object> map6 = new HashMap();map6.put( "button", "7"); map6.put( "desc", "买家申请退款");salerOrderStatusMap.put( "112", map6);
        Map<String, Object> map7 = new HashMap();map7.put( "button", null);map7.put( "desc", "已完成");salerOrderStatusMap.put( "213", map7);
        Map<String, Object> map8 = new HashMap();map8.put( "button", "8"); map8.put( "desc", "进行中");salerOrderStatusMap.put( "104", map8);
        Map<String, Object> map9 = new HashMap();map9.put( "button", null);map9.put( "desc", "等待买家确认完成");salerOrderStatusMap.put( "114", map9);

        Map<String, Object> m =  new HashMap(); m.put( "button", "1,2");  m.put( "desc", "等待买家付款");buyerOrderStatusMap.put( "000", m);
        Map<String, Object> m1 = new HashMap();m1.put( "button", null);  m1.put( "desc", "关闭交易");buyerOrderStatusMap.put( "305", m1);
        Map<String, Object> m2 = new HashMap();m2.put( "button", "3");   m2.put( "desc", "进行中");buyerOrderStatusMap.put( "101", m2);
        Map<String, Object> m3 = new HashMap();m3.put( "button", "4");   m3.put( "desc", "已申请退款");buyerOrderStatusMap.put( "102", m3);
        Map<String, Object> m4 = new HashMap();m4.put( "button", null);  m4.put( "desc", "退款成功");buyerOrderStatusMap.put( "322", m4);
        Map<String, Object> m5 = new HashMap();m5.put( "button", "3,5"); m5.put( "desc", "卖家已投放完成");buyerOrderStatusMap.put( "111", m5);
        Map<String, Object> m6 = new HashMap();m6.put( "button", "4");   m6.put( "desc", "已申请退款");buyerOrderStatusMap.put( "112", m6);
        Map<String, Object> m7 = new HashMap();m7.put( "button", null);  m7.put( "desc", "已完成");buyerOrderStatusMap.put( "213", m7);
        Map<String, Object> m8 = new HashMap();m8.put( "button", "3");   m8.put( "desc", "进行中");buyerOrderStatusMap.put( "104", m8);
        Map<String, Object> m9 = new HashMap();m9.put( "button", "3,5"); m9.put( "desc", "卖家已投放完成");buyerOrderStatusMap.put( "114", m9);

    }

    /**
     * 根据订单状态获取客户端展示的卖家按钮及文案
     * @param multiOrderStatus 订单状态+卖家状态+买家状态 eg: 买家已付款，订单状态、卖家状态、买家状态分别为：1、0、1，则参数为：101
     * @return
     */
    public Map<String, Object> getSalerOrderStatus( String multiOrderStatus){
        return (Map<String, Object>)salerOrderStatusMap.get( multiOrderStatus);
    }

    /**
     * 根据订单状态获取客户端展示的买家按钮及文案
     * @param multiOrderStatus 订单状态+卖家状态+买家状态 eg: 买家已付款，订单状态、卖家状态、买家状态分别为：1、0、1，则参数为：101
     * @return
     */
    public Map<String, Object> getBuyerOrderStatus( String multiOrderStatus){
        return (Map<String, Object>)buyerOrderStatusMap.get( multiOrderStatus);
    }

    @Override
    public void updateStatusBuyerPayment(Long ptbOrderId,Long userId, String orderNo) throws Exception {
        int order_status = 1;//进行中
        int buyer_status = 1; //已支付;
        Date date = new Date();
        Order order = new Order();
        order.setPtbOrderId(ptbOrderId);
        order.setOrderStatus(order_status);
        order.setBuyerStatus(buyer_status);
        order.setLastModifyTime( date);
        order.setLastModifierId( userId);
        int updateCnt = orderMapper.updateByPrimaryKeySelective(order);
        if ( updateCnt < 1){
            throw new Exception("更新订单状态失败");
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderNo(orderNo);
        orderLog.setActionType(OrderActionEnum.BUYER_PAY.getOrderAction());
        orderLog.setCreateTime(date);
        orderLog.setUserId(userId);
        orderLog.setUserType(1);
        orderLog.setRemarks("买家付款,订单关闭");
        orderLogMapper.insertSelective(orderLog);
    }

    @Override
    public void updateStaterefund(Long ptbOrderId, Long userId, String orderNo) throws Exception {
        int buyer_status = 3;//申请退款
        Date date = new Date();
        Order order = new Order();
        order.setBuyerStatus(buyer_status);
        order.setOrderNo(orderNo);
        order.setPtbOrderId(ptbOrderId);
        order.setLastModifyTime(date);
        order.setLastModifierId(userId);
        int i = orderMapper.updateByPrimaryKey(order);
        if (i < 1){
            throw new Exception("退款订单更新失败");
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderNo(orderNo);
        orderLog.setActionType(OrderActionEnum.BUYER_APPLY_REFUND.getOrderAction());
        orderLog.setCreateTime(date);
        orderLog.setPtbOrderLogId(ptbOrderId);
        orderLog.setUserId(userId);
        orderLog.setUserType(1);
        orderLog.setRemarks("买家申请退款，订单关闭");
        orderLogMapper.insertSelective(orderLog);



    }

    @Override
    public boolean checkOrderStatus( OrderActionEnum orderAction, int orderStatus, int salerStatus, int buyerStatus) {
        if ( OrderActionEnum.BUYER_SUBMIT_ORDER == orderAction){
            return true;
        }
        if ( OrderActionEnum.BUYER_CANCAL_ORDER == orderAction){
            return 0==orderStatus && 0==salerStatus && 0==buyerStatus;
        }
        if ( OrderActionEnum.SALER_MODIFY_PRICE == orderAction){
            return 0==orderStatus && 0==salerStatus && 0==buyerStatus;
        }
        if ( OrderActionEnum.BUYER_PAY == orderAction){
            return 0==orderStatus && 0==salerStatus && 0==buyerStatus;
        }
        if ( OrderActionEnum.SALER_COMPLETE == orderAction){
            return 1==orderStatus && 0==salerStatus && (1==buyerStatus||4==buyerStatus);
        }
        if ( OrderActionEnum.BUYER_COMPLETE == orderAction){
            return 1==orderStatus && 1==salerStatus && (1==buyerStatus||4==buyerStatus);
        }
        if ( OrderActionEnum.BUYER_APPLY_REFUND == orderAction){
            return 1==orderStatus && (1==salerStatus||0==salerStatus) && (1==buyerStatus||4==buyerStatus);
        }
        if ( OrderActionEnum.BUYER_CANCEL_REFUND == orderAction){
            return 1==orderStatus && (1==salerStatus||0==salerStatus) && (2==buyerStatus);
        }
        if( OrderActionEnum.SALER_AGREE_REFUND == orderAction){
            return 1==orderStatus && (0==salerStatus||1==salerStatus) && 2==buyerStatus;
        }
        return false;
    }

    @Override
    public void updateStatusForArgeeRefund(Long ptbOrderId, Long salerId, String orderNo) throws Exception {
        int salerStatus = 2;//同意退款
        int orderStatus = 3;//关闭
        Date date = new Date();
        Order order = new Order();
        order.setPtbOrderId( ptbOrderId);
        order.setSellerStatus( salerStatus);
        order.setOrderStatus( orderStatus);
        order.setLastModifyTime( date);
        order.setLastModifierId( salerId);
        int updateCnt = orderMapper.updateByPrimaryKeySelective( order);
        if ( updateCnt < 1){
            throw new Exception("更新订单状态失败");
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderNo( orderNo);
        orderLog.setActionType( OrderActionEnum.SALER_AGREE_REFUND.getOrderAction());
        orderLog.setCreateTime( date);
        orderLog.setUserId( salerId);
        orderLog.setUserType( 2);//卖家
        orderLog.setRemarks( "卖家同意退款,订单关闭");
        orderLogMapper.insertSelective( orderLog);
    }

    @Override
    public void updateStatusForCancelRefund(Long ptbOrderId, Long buyerId, String orderNo) throws Exception {
        int buyerStatus = 4;//取消申请退款
        Date date = new Date();
        Order order = new Order();
        order.setPtbOrderId( ptbOrderId);
        order.setBuyerStatus( buyerStatus);
        order.setLastModifyTime( date);
        order.setLastModifierId( buyerId);
        int updateCnt = orderMapper.updateByPrimaryKeySelective( order);
        if ( updateCnt < 1){
            throw new Exception("更新订单状态失败");
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderNo( orderNo);
        orderLog.setActionType( OrderActionEnum.BUYER_CANCEL_REFUND.getOrderAction());
        orderLog.setCreateTime( date);
        orderLog.setUserId( buyerId);
        orderLog.setUserType( 1);//买家
        orderLog.setRemarks( "买家取消申请退款");
        orderLogMapper.insertSelective( orderLog);
    }
}
