package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.OrderLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface OrderLogMapper extends MyMapper<OrderLog> {
    int deleteByPrimaryKey(Long ptbOrderLogId);

    int insert(OrderLog record);

    int insertSelective(OrderLog record);

    OrderLog selectByPrimaryKey(Long ptbOrderLogId);

    int updateByPrimaryKeySelective(OrderLog record);

    int updateByPrimaryKey(OrderLog record);

    @Select("select * from ptb_order_log where order_no=#{orderNo} order by create_time")
    @ResultMap("BaseResultMap")
    List<OrderLog> selectByOrderNo(@Param("orderNo") String orderNo);
}