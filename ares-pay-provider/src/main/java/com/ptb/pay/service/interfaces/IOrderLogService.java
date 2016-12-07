package com.ptb.pay.service.interfaces;

import com.ptb.pay.vo.order.OrderLogVO;

import java.util.List;

/**
 * Created by watson zhang on 2016/12/7.
 */
public interface IOrderLogService {

    List<OrderLogVO> getOrderLogByOrderId(String orderNo);
}
