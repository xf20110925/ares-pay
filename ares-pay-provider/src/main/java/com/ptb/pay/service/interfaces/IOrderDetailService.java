package com.ptb.pay.service.interfaces;

import com.ptb.pay.model.order.OrderDetail;
import com.ptb.pay.vo.order.OrderDetailVO;

import java.util.List;

/**
 * Created by watson zhang on 2016/11/20.
 */
public interface IOrderDetailService {

    /**
     * 获取订单详情
     * @param orderNo
     * @return
     */
    OrderDetailVO getOrderDetail(String orderNo);

    /**
     * 插入订单详情表
     * @param orderDetailVO
     * @return
     */
    public int insertOrderDetail(OrderDetailVO orderDetailVO);

    public OrderDetailVO convertOrderDetailVO(String orderNo, long origPrice, long payAblePrice, long productId);

    Long getProductIdByOrderNo(String orderNo);

    /**
     * 获取订单对应的商品ID列表
     * @param orderNoList
     * @return
     */
    List<OrderDetail> getOrderDetailList(List<String> orderNoList);
}
