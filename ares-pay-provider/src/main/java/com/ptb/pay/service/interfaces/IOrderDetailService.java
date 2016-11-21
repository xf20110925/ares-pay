package com.ptb.pay.service.interfaces;

import com.ptb.pay.vo.order.OrderDetailVO;

/**
 * Created by watson zhang on 2016/11/20.
 */
public interface IOrderDetailService {

    /**
     * 插入订单详情表
     * @param orderDetailVO
     * @return
     */
    public int insertOrderDetail(OrderDetailVO orderDetailVO);


    public OrderDetailVO convertOrderDetailVO(String orderNo, long origPrice, long payAblePrice, long productId);

    Long getProductIdByOrderNo(String orderNo);
}
