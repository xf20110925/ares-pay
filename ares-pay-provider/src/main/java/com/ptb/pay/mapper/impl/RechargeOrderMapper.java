package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.model.RechargeOrderExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RechargeOrderMapper extends MyMapper<RechargeOrder> {
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

    @Select("select * from ptb_recharge_order where ptb_recharge_order_id = #{rechargeOrderId} and user_id = #{userId}")
    @ResultMap("BaseResultMap")
    RechargeOrder selectByIdAndUserId( @Param("rechargeOrderId")Long rechargeOrderId, @Param("userId") Long userId);
}