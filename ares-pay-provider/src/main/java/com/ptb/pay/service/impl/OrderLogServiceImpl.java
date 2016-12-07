package com.ptb.pay.service.impl;

import com.ptb.pay.mapper.impl.OrderLogMapper;
import com.ptb.pay.model.OrderLog;
import com.ptb.pay.service.interfaces.IOrderLogService;
import com.ptb.pay.vo.order.OrderLogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by watson zhang on 2016/12/7.
 */
@Service("orderLogServiceImpl")
public class OrderLogServiceImpl implements IOrderLogService{
    private static Logger logger = LoggerFactory.getLogger(OrderLogServiceImpl.class);

    @Autowired
    OrderLogMapper orderLogMapper;

    @Override
    public List<OrderLogVO> getOrderLogByOrderId(String orderNo) {
        if (orderNo == null){
            logger.error("order no is null!");
            return null;
        }
        List<OrderLog> orderLogs = orderLogMapper.selectByOrderNo(orderNo);
        if (orderLogs == null){
            logger.error("can not get order logs by order: {}!", orderNo);
            return null;
        }

        List<OrderLogVO> orderLogVOs = new ArrayList<>();
        orderLogs.forEach(log -> {
            OrderLogVO orderLogVO = new OrderLogVO();
            orderLogVO.setPtbOrderLogId(log.getPtbOrderLogId());
            orderLogVO.setActionType(log.getActionType());
            orderLogVO.setCreateTime(log.getCreateTime().getTime());
            orderLogVO.setOrderNo(log.getOrderNo());
            orderLogVO.setRemarks(log.getRemarks());
            orderLogVO.setUserId(log.getUserId());
            orderLogVO.setUserType(log.getUserType());
            orderLogVOs.add(orderLogVO);
        });
        return orderLogVOs;
    }
}
