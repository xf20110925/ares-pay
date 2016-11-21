package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.order.OrderDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface OrderDetailMapper extends MyMapper<OrderDetail>{

    @Select("select * from ptb_order_detail where order_no=#{orderNo}")
    OrderDetail selectByOrderNo(@Param("orderNo") String orderNo);
}