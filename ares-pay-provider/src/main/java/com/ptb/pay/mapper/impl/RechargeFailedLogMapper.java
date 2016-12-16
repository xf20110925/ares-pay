package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.RechargeFailedLog;
import com.ptb.pay.model.RechargeFailedLogExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface RechargeFailedLogMapper extends MyMapper<RechargeFailedLog> {
    int deleteByExample(RechargeFailedLogExample example);

    int deleteByPrimaryKey(Long ptbRechargeFailedLogId);

    int insert(RechargeFailedLog record);

    int insertSelective(RechargeFailedLog record);

    List<RechargeFailedLog> selectByExample(RechargeFailedLogExample example);

    RechargeFailedLog selectByPrimaryKey(Long ptbRechargeFailedLogId);

    int updateByExampleSelective(@Param("record") RechargeFailedLog record, @Param("example") RechargeFailedLogExample example);

    int updateByExample(@Param("record") RechargeFailedLog record, @Param("example") RechargeFailedLogExample example);

    int updateByPrimaryKeySelective(RechargeFailedLog record);

    int updateByPrimaryKey(RechargeFailedLog record);
}