package com.ptb.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.ptb.pay.mapper.impl.RechargeOrderLogMapper;
import com.ptb.pay.model.RechargeOrderLog;
import com.ptb.pay.service.interfaces.IRechargeOrderLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Description: 充值订单操作日志业务实现类
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:11  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Service("rechargeOrderLogService")
@Transactional
public class RechargeOrderLogServiceImpl implements IRechargeOrderLogService {

    private static Logger LOGGER = LoggerFactory.getLogger(RechargeOrderLogServiceImpl.class);

    @Autowired
    private RechargeOrderLogMapper rechargeOrderLogMapper;

    @Override
    public boolean saveUserOpLog(String rechargeOrderNo, Integer actionType, String remarks, Long userId) throws Exception {
        return saveLog(rechargeOrderNo, actionType, remarks, userId, 1);
    }

    @Override
    public boolean saveAdminOpLog(String rechargeOrderNo, Integer actionType, String remarks, Long adminId) throws Exception {
        return saveLog(rechargeOrderNo, actionType, remarks, adminId, 2);
    }

    @Async
    public boolean saveLog(String rechargeOrderNo, Integer actionType, String remarks, Long userId, Integer userType) {
        RechargeOrderLog log = new RechargeOrderLog();
        try {
            log.setActionType(actionType);
            log.setCreateTime(new Date());
            log.setRechargeOrderNo(rechargeOrderNo);
            log.setRemarks(remarks);
            log.setUserId(userId);
            log.setUserType(userType);
            rechargeOrderLogMapper.insert(log);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("记录充值订单操作日志失败, params : " + JSON.toJSONString(log), e);
            return false;
        }

    }
}
