package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.Order;
import com.ptb.pay.vo.order.OrderQueryVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by zuokui.fu on 2016/11/16.
 */
@Component
public interface OrderMapper extends MyMapper<Order> {
    int insertReturnId(Order order);

    @Select( "select * from ptb_order where order_no = #{orderNo}")
    @ResultMap("BaseResultMap")
    Order getOrderByOrderNo( @Param("orderNo") String orderNo);

    @Update(" update ptb_order set order_status = #{orderStatus}, buyer_status = #{buyerStatus}, last_modify_time=#{updateTime} where ptb_order_id = #{orderId}")
    int updateOrderStateByOrderNo(@Param("orderId") long orderId, @Param("orderStatus") int orderStatus, @Param("buyerStatus") int buyerStatus, @Param("updateTime")Date updateTime);

    @Select("select * from ptb_order where seller_id=#{uid} order by create_time desc")
    @ResultMap("BaseResultMap")
    List<Order> selectBySellerUid(@Param("uid") long userId);

    @Select("select * from ptb_order where buyer_id=#{uid} order by create_time desc")
    @ResultMap("BaseResultMap")
    List<Order> selectByBuyerUid(@Param("uid") long userId);

    @Update("UPDATE ptb_order SET payable_price=#{price} WHERE ptb_order_id=#{orderId}")
    @ResultMap("BaseResultMap")
    int updateOrderPriceByOrderId(@Param("orderId") long orderId, @Param("price") long price);

    @ResultMap("BaseResultMap")
    List<Order> selectDynamics(OrderQueryVO orderQueryVO);

    @Select("select * from ptb_order where seller_id = #{userId} and last_modify_time BETWEEN #{beginDate} and #{endDate} and last_modifier_id != #{userId}")
    @ResultMap("BaseResultMap")
    List<Order> getSellerOrderChangeList( @Param("userId") Long userId, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

    @Select("select * from ptb_order where buyer_id = #{userId} and last_modify_time BETWEEN #{beginDate} and #{endDate} and last_modifier_id != #{userId}")
    @ResultMap("BaseResultMap")
    List<Order> getBuyerOrderChangeList(  @Param("userId") Long userId, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);
}
