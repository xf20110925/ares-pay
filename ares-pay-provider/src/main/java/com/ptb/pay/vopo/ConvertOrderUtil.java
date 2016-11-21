package com.ptb.pay.vopo;

import com.ptb.pay.model.Order;
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
}
