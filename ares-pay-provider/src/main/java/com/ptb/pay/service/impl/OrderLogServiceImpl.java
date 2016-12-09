package com.ptb.pay.service.impl;

import com.ptb.pay.enums.OrderActionEnum;
import com.ptb.pay.mapper.impl.OrderLogMapper;
import com.ptb.pay.model.OrderLog;
import com.ptb.pay.service.interfaces.IOrderLogService;
import com.ptb.pay.service.interfaces.IOrderService;
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
    @Autowired
    IOrderService iOrderService;

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
        for (OrderLog orderLog : orderLogs) {
            OrderLogVO orderLogVO = new OrderLogVO();
            orderLogVO.setPtbOrderLogId(orderLog.getPtbOrderLogId());
            orderLogVO.setActionType(orderLog.getActionType());
            orderLogVO.setActionName(OrderActionEnum.getOrderActionInfo(orderLog.getActionType()).getDesc());
            orderLogVO.setCreateTime(orderLog.getCreateTime().getTime());
            orderLogVO.setOrderNo(orderLog.getOrderNo());
            orderLogVO.setRemarks(orderLog.getRemarks());
            orderLogVO.setUserId(orderLog.getUserId());
            orderLogVO.setUserType(orderLog.getUserType());
            orderLogVOs.add(orderLogVO);
        }
        return orderLogVOs;
    }
}
