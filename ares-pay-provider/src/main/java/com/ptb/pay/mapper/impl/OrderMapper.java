package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

/**
 * Created by zuokui.fu on 2016/11/16.
 */
public interface OrderMapper extends MyMapper<Order> {

    @Select( "select * from ptb_order where order_no = #{orderNo}")
    @ResultMap("BaseResultMap")
    Order getOrderByOrderNo( @Param("orderNo") String orderNo);
}
