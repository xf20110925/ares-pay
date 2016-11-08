package com.ptb.pay.mapper.impl;

import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.model.RechargeOrderExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RechargeOrderMapper {
    int deleteByExample(RechargeOrderExample example);

    int deleteByPrimaryKey(Long ptbRechargeOrderId);

    int insert(RechargeOrder record);

    int insertSelective(RechargeOrder record);

    List<RechargeOrder> selectByExample(RechargeOrderExample example);

    RechargeOrder selectByPrimaryKey(Long ptbRechargeOrderId);

    int updateByExampleSelective(@Param("record") RechargeOrder record, @Param("example") RechargeOrderExample example);

    int updateByExample(@Param("record") RechargeOrder record, @Param("example") RechargeOrderExample example);

    int updateByPrimaryKeySelective(RechargeOrder record);

    int updateByPrimaryKey(RechargeOrder record);
}