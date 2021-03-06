package com.ptb.pay.service.impl;

import com.ptb.pay.mapper.impl.OrderDetailMapper;
import com.ptb.pay.model.order.OrderDetail;
import com.ptb.pay.service.interfaces.IOrderDetailService;
import com.ptb.pay.vo.order.OrderDetailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by watson zhang on 2016/11/20.
 */
@Service("orderDetailService")
public class OrderDetailServiceImpl implements IOrderDetailService{
    private static Logger logger = LoggerFactory.getLogger(OrderDetailServiceImpl.class);

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Override
    public OrderDetailVO getOrderDetail(String orderNo) {
        OrderDetail orderDetailByOrderNo = orderDetailMapper.getOrderDetailByOrderNo(orderNo);
        if (orderDetailByOrderNo == null){
            logger.error("get order detail error!");
            return null;
        }
        OrderDetailVO orderDetailVO = new OrderDetailVO(orderDetailByOrderNo.getPtbOrderDetailId(),
                orderDetailByOrderNo.getOrderNo(),
                orderDetailByOrderNo.getOriginalPrice(),
                orderDetailByOrderNo.getPayablePrice(),
                orderDetailByOrderNo.getProductId());
        return orderDetailVO;
    }

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
        OrderDetail orderDetail = orderDetailMapper.getOrderDetailByOrderNo(orderNo);
        if(null != orderDetail)
            return orderDetail.getProductId();
        return null;
    }

    @Override
    public List<OrderDetail> getOrderDetailList(List<String> orderNoList) {
        return orderDetailMapper.selectAllByOrderNos(orderNoList);
    }
}
