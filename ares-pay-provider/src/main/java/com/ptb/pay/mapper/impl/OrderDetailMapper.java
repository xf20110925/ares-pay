package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.order.OrderDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderDetailMapper extends MyMapper<OrderDetail>{

    @Select("select * from ptb_order_detail where order_no = #{orderNo}")
    @ResultMap("BaseResultMap")
    OrderDetail getOrderDetailByOrderNo(@Param("orderNo") String orderNo);

    @ResultMap("BaseResultMap")
    List<OrderDetail> selectAllByOrderNos(@Param("orderNos") List<String> orderNoList);
}