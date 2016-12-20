package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.RechargeOrderLog;
import com.ptb.pay.model.RechargeOrderLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeOrderLogMapper extends MyMapper<RechargeOrderLog> {
    int deleteByExample(RechargeOrderLogExample example);

    int deleteByPrimaryKey(Long ptbOrderLogId);

    int insert(RechargeOrderLog record);

    int insertSelective(RechargeOrderLog record);

    List<RechargeOrderLog> selectByExample(RechargeOrderLogExample example);

    RechargeOrderLog selectByPrimaryKey(Long ptbOrderLogId);

    int updateByExampleSelective(@Param("record") RechargeOrderLog record, @Param("example") RechargeOrderLogExample example);

    int updateByExample(@Param("record") RechargeOrderLog record, @Param("example") RechargeOrderLogExample example);

    int updateByPrimaryKeySelective(RechargeOrderLog record);

    int updateByPrimaryKey(RechargeOrderLog record);
}