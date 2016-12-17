package com.ptb.pay.service.impl;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IProductApi;
import com.ptb.pay.enums.*;
import com.ptb.pay.mapper.impl.OrderDetailMapper;
import com.ptb.pay.mapper.impl.OrderLogMapper;
import com.ptb.pay.mapper.impl.OrderMapper;
import com.ptb.pay.model.Order;
import com.ptb.pay.model.OrderLog;
import com.ptb.pay.service.interfaces.IOrderDetailService;
import com.ptb.pay.service.interfaces.IOrderService;
import com.ptb.pay.vo.order.OrderTimeAxis;
import com.ptb.ucenter.api.IBindMediaApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zuokui.fu on 2016/11/16.
 */
@Service("orderService")
public class OrderServiceImpl implements IOrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderLogMapper orderLogMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Resource
    private IOrderDetailService orderDetailService;

    @Resource
    private IProductApi productApi;

    @Resource
    private IBindMediaApi bindMediaApi;

    private static Map<String, Object> salerOrderStatusMap = new HashMap<>();
    private static Map<String, Object> buyerOrderStatusMap = new HashMap<>();
    private static Map<Integer, String> orderActionTimeAxis = new HashMap<>();

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

        orderActionTimeAxis.put(OrderActionEnum.BUYER_SUBMIT_ORDER.getOrderAction(), "创建");
        orderActionTimeAxis.put(OrderActionEnum.BUYER_PAY.getOrderAction(), "付款");
        orderActionTimeAxis.put(OrderActionEnum.BUYER_APPLY_REFUND.getOrderAction(), "申请退款");
        orderActionTimeAxis.put(OrderActionEnum.BUYER_CANCEL_REFUND.getOrderAction(), "取消申请退款");
        orderActionTimeAxis.put(OrderActionEnum.SALER_COMPLETE.getOrderAction(), "投放完成");
        orderActionTimeAxis.put(OrderActionEnum.BUYER_COMPLETE.getOrderAction(), "订单完成");
        orderActionTimeAxis.put(OrderActionEnum.SALER_AGREE_REFUND.getOrderAction(), "订单完成");
        orderActionTimeAxis.put(OrderActionEnum.BUYER_CANCAL_ORDER.getOrderAction(), "订单完成");
        orderActionTimeAxis.put(OrderActionEnum.SALER_MODIFY_PRICE.getOrderAction(), "卖家修改价格");
        orderActionTimeAxis.put(OrderActionEnum.ADMIN_COMPLETE_ORDER.getOrderAction(), "订单完成");
        orderActionTimeAxis.put(OrderActionEnum.ADMIN_CANCEL_ORDER.getOrderAction(), "订单完成");

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
    public String getOrderStatusName(int orderAction){
        return orderActionTimeAxis.get(orderAction);
    }

    @Override
    public Order insertNewOrder(long buyerId, long sellerId, long price, String orderId, String desc) throws Exception {
        if (buyerId <= 0 || sellerId <= 0 || orderId == null){
            logger.error("insert order error! buyerId:{} sellerId:{} orderId:{}", buyerId, sellerId, orderId);
            return null;
        }
        Date date = new Date();
        Order order = new Order();
        order.setOrderNo( orderId);
        order.setOrderStatus( OrderStatusEnum.ORDER_STATUS_NEW_DEAL.getStatus());
        order.setSellerStatus( SellerStatusEnum.SELLER_STATUS_INIT.getStatus());
        order.setBuyerStatus( BuyerStatusEnum.BUYER_STATUS_INIT.getStatus());
        order.setOriginalPrice(price);
        order.setPayablePrice(price);
        order.setSellerId( sellerId);
        order.setRemarks(desc);
        order.setBuyerId( buyerId);
        order.setCreateTime( date);
        order.setLastModifyTime( date);
        order.setLastModifierId( buyerId);
        int insertCnt = orderMapper.insertReturnId( order);
        if ( insertCnt < 1){
            throw new Exception("更新订单状态失败");
        }
        String remarks = "买家提交订单";
        this.insertOrderLog(orderId, OrderActionEnum.BUYER_SUBMIT_ORDER.getOrderAction(), date, remarks, buyerId, UserTypeEnum.USER_IS_BUYER.getUserType());

       return order;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public Order cancelOrderByBuyer(long buyerId, long orderId) throws Exception {
        if (buyerId <= 0 || orderId <= 0){
            logger.error("cancel order error! buyerId:{} orderId:{}", buyerId, orderId);
            return null;
        }
        Date date = new Date();
        int update = orderMapper.updateOrderStateByOrderNo(orderId, OrderStatusEnum.ORDER_STATUS_DEAL_CLOSE.getStatus(), BuyerStatusEnum.BUYER_STATUS_CANCLE_ORDER.getStatus(), date);
        if (update < 1){
            logger.error("buyer cacle order error! buyer:{}  orderId:{}", buyerId, orderId);
            throw new RuntimeException("buyer cacle order error!");
        }
        String remarks = "买家取消订单";
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null){
            logger.error("get order by orderId error! orderId:{}", orderId);
            throw new RuntimeException("get order by orderId error!");
        }
        this.insertOrderLog(order.getOrderNo(), OrderActionEnum.BUYER_CANCAL_ORDER.getOrderAction(), date, remarks, buyerId, UserTypeEnum.USER_IS_BUYER.getUserType());

        return order;
    }

    @Override
    public Order getOrderByOrderId(long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null){
            logger.error("get order by orderId error! orderId:{}", order);
            return null;
        }
        return order;
    }

    @Override
    @Transactional
    public int changeOrderPrice(long userId, long orderId, long price) {
        Order orderFirst = orderMapper.selectByPrimaryKey(orderId);
        if (orderFirst == null){
            logger.error("获取订单出错");
            throw new RuntimeException("获取订单出错");
        }

        int ret = orderDetailMapper.updateProductPriceByOrderNo(orderFirst.getOrderNo(), price);
        if (ret < 1){
            logger.error("更新订单detail价格出错");
            throw new RuntimeException("更新订单detail价格出错");
        }

        int update = orderMapper.updateOrderPriceByOrderId(orderId, price);
        if (update < 1){
            logger.error("更新订单价格出错");
            throw new RuntimeException("更新订单价格出错");
        }
        Order order = orderMapper.selectByPrimaryKey(orderId);
        String remarks = String.format("卖家修改价格 price:%d", price);
        this.insertOrderLog(order.getOrderNo(), OrderActionEnum.SALER_MODIFY_PRICE.getOrderAction(), new Date(), remarks, userId, UserTypeEnum.USER_IS_SELLER.getUserType());
        return update;
    }

    @Override
    @Transactional
    public boolean sellerConfirmOrder(long seller, Order order) {
        //修改订单状态
        Date date = new Date();
        order.setSellerStatus(SellerStatusEnum.SELLER_STATUS_CONFIRM.getStatus());
        order.setLastModifierId(order.getSellerId());
        order.setLastModifyTime(date);
        int ia = orderMapper.updateByPrimaryKeySelective(order);

        if(ia < 1)
            throw new RuntimeException("卖家确认完成更新订单状态失败");

        //增加操作日志
        OrderLog orderLog = new OrderLog();
        orderLog.setActionType(OrderActionEnum.SALER_COMPLETE.getOrderAction());
        orderLog.setCreateTime(date);
        orderLog.setOrderNo(order.getOrderNo());
        orderLog.setRemarks(OrderActionEnum.SALER_COMPLETE.getDesc());
        orderLog.setUserType(UserTypeEnum.USER_IS_SELLER.getUserType());
        orderLog.setUserId(order.getSellerId());
        ia = orderLogMapper.insert(orderLog);
        if(ia < 1)
            throw new RuntimeException("卖家确认完成更新订单操作日志失败");

        return true;
    }

    @Override
    @Transactional
    public boolean buyerConfirmOrder(long userId, Order order) {
        ResponseVo responseVo1;

        //更新商品计数
        Long productId = orderDetailService.getProductIdByOrderNo(order.getOrderNo());
        if(productId != null) {
            responseVo1 = productApi.updateProductDealNum(userId, productId);
            if (!responseVo1.getCode().equals("0")) {
                logger.error("buyerConfirm, update productDealNum error, orderNo:" + order.getOrderNo() + " productId:" + productId);
                throw new RuntimeException("买家确认完成商品交易量更新失败");
            }
        }else{
            logger.error("buyerConfirm, product not exists of orderNo:" + order.getOrderNo());
            throw new RuntimeException("买家确认完成更新商品交易量，商品丢失");
        }

        //修改订单状态
        Date date = new Date();
        order.setOrderStatus(OrderStatusEnum.ORDER_STATUS_DEAL_OVER.getStatus());
        order.setBuyerStatus(BuyerStatusEnum.BUYER_STATUS_CONFIRM.getStatus());
        order.setLastModifierId(order.getSellerId());
        order.setLastModifyTime(date);
        order.setPayTime(date);
        int ia = orderMapper.updateByPrimaryKeySelective(order);
        if(ia < 1)
            throw new RuntimeException("买家确认完成更新订单状态失败");

        //增加操作日志
        OrderLog orderLog = new OrderLog();
        orderLog.setActionType(OrderActionEnum.BUYER_COMPLETE.getOrderAction());
        orderLog.setCreateTime(date);
        orderLog.setOrderNo(order.getOrderNo());
        orderLog.setRemarks(OrderActionEnum.BUYER_COMPLETE.getDesc());
        orderLog.setUserType(UserTypeEnum.USER_IS_BUYER.getUserType());
        orderLog.setUserId(order.getBuyerId());
        ia = orderLogMapper.insert(orderLog);
        if(ia < 1)
            throw new RuntimeException("买家确认完成更新订单操作日志失败");

        //上报用户中心交易成功
        try {
            responseVo1 = bindMediaApi.reportDealInfo(order.getSellerId(), order.getBuyerId(), 0);
            if(!responseVo1.getCode().equals("0")){
                //更新失败 add message to bus
                logger.error("buyerConfirm, report dealSuccess to bindMediaApi orderNo:" + order.getOrderNo() + " sellerId:" + order.getSellerId() + " buyerId:" + order.getBuyerId());
                throw new RuntimeException("买家确认完成上报uCenter失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("买家确认完成上报uCenter发生异常");
        }

        return true;
    }

    @Override
    public int getOrderStatus(long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null){
            return -1;
        }
        return order.getOrderStatus();
    }

    @Override
    public List<OrderTimeAxis> getOrderTimeAxises(String orderNo) {
        return  orderLogMapper.selectByOrderNo(orderNo).stream().map(item->{
            String desc = orderActionTimeAxis.get(item.getActionType());
            return desc == null?null:new OrderTimeAxis(item.getCreateTime().getTime(), desc);
        }).filter(timePoint->timePoint!=null).collect(Collectors.toList());
    }

    public int insertOrderLog(String orderNo, int actionType, Date createTime, String remarks, long userID, int userType){
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderNo( orderNo);
        orderLog.setActionType(actionType);
        orderLog.setCreateTime( createTime);
        orderLog.setUserId( userID);
        orderLog.setUserType( userType);
        orderLog.setRemarks( remarks);
        int ret = orderLogMapper.insertSelective(orderLog);
        if (ret < 1){
            logger.error("insert order log error! orderNo:{} userId:{} userType:{}", orderNo, userID, userType);
            return -1;
        }
        return ret;
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
        orderLog.setRemarks("买家付款!");
        int i = orderLogMapper.insertSelective(orderLog);
        if (i < 1){
            logger.error("buyerPayment insert order log error! orderNo:{} userId:{}",orderNo,userId);
            throw new Exception("买家付款日志插入失败");
        }
    }

    @Override
    public void updateStaterefund(Long ptbOrderId, Long userId, String orderNo) throws Exception {
        int buyer_status = 2;//申请退款
        Date date = new Date();
        Order order = new Order();
        order.setBuyerStatus(buyer_status);
        order.setOrderNo(orderNo);
        order.setPtbOrderId(ptbOrderId);
        order.setLastModifyTime(date);
        order.setLastModifierId(userId);
        int i = orderMapper.updateByPrimaryKeySelective(order);
        if (i < 1){
            throw new Exception("退款订单更新失败");
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderNo(orderNo);
        orderLog.setActionType(OrderActionEnum.BUYER_APPLY_REFUND.getOrderAction());
        orderLog.setCreateTime(date);
        orderLog.setUserId(userId);
        orderLog.setUserType(1);
        orderLog.setRemarks("买家申请退款!");
        int i1 = orderLogMapper.insertSelective(orderLog);
        if (i1 < 1){
            logger.error("refund insert order log error!orderNo:{}userId:{}",orderNo,userId);
            throw new Exception("买家申请退款日志插入失败");
        }


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
        updateCnt = orderLogMapper.insertSelective( orderLog);
        if ( updateCnt < 1){
            throw new Exception("卖家同意退款插入日志失败");
        }
    }

    @Override
    public void updateStatusForAdminRefund(Long ptbOrderId, Long adminId, String orderNo, String reason) throws Exception {
         int salerStatus = 2;//同意退款
        int orderStatus = 3;//关闭
        Date date = new Date();
        Order order = new Order();
        order.setPtbOrderId( ptbOrderId);
        order.setSellerStatus( salerStatus);
        order.setOrderStatus( orderStatus);
        order.setLastModifyTime( date);
        order.setLastModifierId( adminId);
        int updateCnt = orderMapper.updateByPrimaryKeySelective( order);
        if ( updateCnt < 1){
            throw new Exception("更新订单状态失败");
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderNo( orderNo);
        orderLog.setActionType( OrderActionEnum.ADMIN_CANCEL_ORDER.getOrderAction());
        orderLog.setCreateTime( date);
        orderLog.setUserId( adminId);
        orderLog.setUserType( UserTypeEnum.USER_IS_ADMIN.getUserType());//卖家
        orderLog.setRemarks(reason);
        updateCnt = orderLogMapper.insertSelective( orderLog);
        if ( updateCnt < 1){
            throw new Exception("卖家同意退款插入日志失败");
        }

    }

    @Override
    public boolean updateStatusForAdminComplete(long adminId, Order order, String reason){
        //修改订单状态
        Date date = new Date();
        order.setOrderStatus(OrderStatusEnum.ORDER_STATUS_DEAL_OVER.getStatus());
        order.setSellerStatus(SellerStatusEnum.SELLER_STATUS_CONFIRM.getStatus());
        order.setBuyerStatus(BuyerStatusEnum.BUYER_STATUS_CONFIRM.getStatus());
        order.setLastModifierId(adminId);
        order.setLastModifyTime(date);
        order.setPayTime(date);
        int ia = orderMapper.updateByPrimaryKeySelective(order);
        if(ia < 1)
            throw new RuntimeException("买家确认完成更新订单状态失败");

        //增加操作日志
        OrderLog orderLog = new OrderLog();
        orderLog.setActionType(OrderActionEnum.ADMIN_COMPLETE_ORDER.getOrderAction());
        orderLog.setCreateTime(date);
        orderLog.setOrderNo(order.getOrderNo());
        orderLog.setRemarks(reason);
        orderLog.setUserType(UserTypeEnum.USER_IS_ADMIN.getUserType());
        orderLog.setUserId(adminId);
        ia = orderLogMapper.insert(orderLog);
        if(ia < 1)
            throw new RuntimeException("买家确认完成更新订单操作日志失败");
        return true;
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
        updateCnt = orderLogMapper.insertSelective( orderLog);
        if ( updateCnt < 1){
            throw new Exception("买家取消申请退款插入日志失败");
        }
    }
}
