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

/**
 * Created by zuokui.fu on 2016/11/16.
 */
@Service("orderService")
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderLogMapper orderLogMapper;

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
        Order order = new Order();
        order.setPtbOrderId( ptbOrderId);
        order.setSellerStatus( 2);
        order.setLastModifyTime( new Date());
        order.setLastModifierId( salerId);
        int updateCnt = orderMapper.updateByPrimaryKeySelective( order);
        if ( updateCnt < 1){
            throw new Exception("更新订单状态失败");
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderNo( orderNo);
        orderLog.setActionType( OrderActionEnum.SALER_AGREE_REFUND.getOrderAction());
        orderLog.setCreateTime( new Date());
        orderLog.setUserId( salerId);
        orderLog.setUserType( 2);
        orderLogMapper.insertSelective( orderLog);
    }
}
