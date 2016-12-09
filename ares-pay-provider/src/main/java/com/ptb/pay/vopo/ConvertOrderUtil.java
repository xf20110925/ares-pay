package com.ptb.pay.vopo;

import com.ptb.pay.enums.OrderActionEnum;
import com.ptb.pay.model.Order;
import com.ptb.pay.model.OrderLog;
import com.ptb.pay.vo.order.OrderLogVO;
import com.ptb.pay.vo.order.OrderVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by MyThinkpad on 2016/11/21.
 */
public class ConvertOrderUtil {

    public static OrderVO convertOrderToOrderVO(Order item){
            OrderVO orderVO = new OrderVO();

            orderVO.setBuyerId(item.getBuyerId());
            orderVO.setBuyerStatus(item.getBuyerStatus());
            orderVO.setCreateTime(item.getCreateTime());
            orderVO.setPayTime(item.getPayTime());
            orderVO.setLastModifierId(item.getLastModifierId());
            orderVO.setLastModifyTime(item.getLastModifyTime());
            orderVO.setOrderNo(item.getOrderNo());
            orderVO.setOrderStatus(item.getOrderStatus());
            orderVO.setSellerId(item.getSellerId());
            orderVO.setSellerStatus(item.getSellerStatus());
            orderVO.setRemarks(item.getRemarks());
            orderVO.setOriginalPrice(item.getOriginalPrice());
            orderVO.setPayablePrice(item.getPayablePrice());
            orderVO.setPtbOrderId(item.getPtbOrderId());
            return orderVO;
    }

        public static OrderLogVO convertOrderLogToVO(OrderLog orderLog){
            OrderLogVO orderLogVO = new OrderLogVO();
            orderLogVO.setPtbOrderLogId(orderLog.getPtbOrderLogId());
            orderLogVO.setActionType(orderLog.getActionType());
            orderLogVO.setActionName(OrderActionEnum.getOrderActionInfo(orderLog.getActionType()).getDesc());
            orderLogVO.setCreateTime(orderLog.getCreateTime().getTime());
            orderLogVO.setOrderNo(orderLog.getOrderNo());
            orderLogVO.setRemarks(orderLog.getRemarks());
            orderLogVO.setUserId(orderLog.getUserId());
            orderLogVO.setUserType(orderLog.getUserType());
            return orderLogVO;
        }

}
