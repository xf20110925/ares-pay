package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.model.ThirdPaymentNotifyLog;
import com.ptb.pay.model.ThirdPaymentNotifyLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ThirdPaymentNotifyLogMapper extends MyMapper<ThirdPaymentNotifyLog> {
    int deleteByExample(ThirdPaymentNotifyLogExample example);

    int deleteByPrimaryKey(Long pthThirdPaymentNotifyLogId);

    int insert(ThirdPaymentNotifyLog record);

    int insertSelective(ThirdPaymentNotifyLog record);

    List<ThirdPaymentNotifyLog> selectByExample(ThirdPaymentNotifyLogExample example);

    ThirdPaymentNotifyLog selectByPrimaryKey(Long pthThirdPaymentNotifyLogId);

    int updateByExampleSelective(@Param("record") ThirdPaymentNotifyLog record, @Param("example") ThirdPaymentNotifyLogExample example);

    int updateByExample(@Param("record") ThirdPaymentNotifyLog record, @Param("example") ThirdPaymentNotifyLogExample example);

    int updateByPrimaryKeySelective(ThirdPaymentNotifyLog record);

    int updateByPrimaryKey(ThirdPaymentNotifyLog record);
}