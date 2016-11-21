package com.ptb.pay.service.impl;

import com.ptb.pay.mapper.impl.OrderDetailMapper;
import com.ptb.pay.model.order.OrderDetail;
import com.ptb.pay.service.interfaces.IOrderDetailService;
import com.ptb.pay.vo.order.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by watson zhang on 2016/11/20.
 */
@Service("orderDetailService")
public class OrderDetailServiceImpl implements IOrderDetailService{

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Override
    public int insertOrderDetail(OrderDetailVO orderDetailVO) {

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderNo( orderDetailVO.getOrderNo());
        orderDetail.setOriginalPrice( orderDetailVO.getOriginalPrice());
        orderDetail.setPayablePrice(orderDetailVO.getPayAblePrice());
        orderDetail.setProductId( orderDetailVO.getProductId());
        int insert = orderDetailMapper.insert(orderDetail);
        return insert;
    }

    @Override
    public OrderDetailVO convertOrderDetailVO(String orderNo, long origPrice, long payAblePrice, long productId){
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setOrderNo(orderNo);
        orderDetailVO.setOriginalPrice(origPrice);
        orderDetailVO.setPayAblePrice(payAblePrice);
        orderDetailVO.setProductId(productId);
        return orderDetailVO;
    }

    @Override
    public Long getProductIdByOrderNo(String orderNo) {
        OrderDetail orderDetail = orderDetailMapper.selectByOrderNo(orderNo);
        if(null != orderDetail)
            return orderDetail.getProductId();
        return null;
    }
}
