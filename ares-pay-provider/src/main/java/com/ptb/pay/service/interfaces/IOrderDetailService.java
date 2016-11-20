package com.ptb.pay.service.interfaces;

import com.ptb.pay.vo.orderdetail.OrderDetailVO;

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
}
