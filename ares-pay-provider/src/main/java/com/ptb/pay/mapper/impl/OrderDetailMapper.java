package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.order.OrderDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface OrderDetailMapper extends MyMapper<OrderDetail>{

    @Select("select * from ptb_order_detail where order_no = #{orderNo}")
    @ResultMap("BaseResultMap")
    OrderDetail getOrderDetailByOrderNo(@Param("orderNo") String orderNo);

    @ResultMap("BaseResultMap")
    List<OrderDetail> selectAllByOrderNos(@Param("orderNos") List<String> orderNoList);

    @Update("update ptb_order_detail set payable_price = #{price} where order_no = #{orderNo}")
    int updateProductPriceByOrderNo(@Param("orderNo") String orderNo, @Param("price") long price);
}