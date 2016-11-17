package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.OrderLog;

public interface OrderLogMapper extends MyMapper<OrderLog> {
    int deleteByPrimaryKey(Long ptbOrderLogId);

    int insert(OrderLog record);

    int insertSelective(OrderLog record);

    OrderLog selectByPrimaryKey(Long ptbOrderLogId);

    int updateByPrimaryKeySelective(OrderLog record);

    int updateByPrimaryKey(OrderLog record);
}