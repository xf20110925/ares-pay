package com.ptb.pay.service.impl;

import com.ptb.common.enums.RechargeOrderStatusEnum;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.service.interfaces.IOnlinePaymentService;
import com.ptb.pay.service.interfaces.IRechargeOrderService;
import com.ptb.pay.service.factory.OnlinePaymentServiceFactory;
import com.ptb.utils.tool.GenerateOrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ptb.pay.vo.RechargeOrderParamsVO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 线上充值接口
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-07 19:44  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Service
@Transactional
public class OnlineRechargeOrderServiceImpl implements IRechargeOrderService{

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Override
    public RechargeOrder createRechargeOrder(RechargeOrderParamsVO paramsVO) throws Exception {

        Date now = new Date();
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setCreateTime(now);
        rechargeOrder.setOrderNo(paramsVO.getOrderNo());
        rechargeOrder.setPayMethod(paramsVO.getPayMethod());
        rechargeOrder.setRechargeOrderNo(GenerateOrderNoUtil.createRechargeOrderNo(paramsVO.getDeviceType(), paramsVO.getPayMethod()));
        rechargeOrder.setStatus(RechargeOrderStatusEnum.unpay.getRechargeOrderStatus());
        rechargeOrder.setTotalAmount(paramsVO.getRechargeAmount());
        rechargeOrder.setUserId(paramsVO.getUserId());
        rechargeOrder.setPayType(paramsVO.getPayType());
        rechargeOrder.setDeviceType(paramsVO.getDeviceType());
        rechargeOrderMapper.insert(rechargeOrder);
        return rechargeOrder;
    }

    @Override
    public Map<String, Object> getReturnData(RechargeOrder rechargeOrder) throws Exception {
        IOnlinePaymentService onlinePaymentService = OnlinePaymentServiceFactory.createService(rechargeOrder.getPayType());
        String orderInfo = onlinePaymentService.getPaymentInfo(rechargeOrder.getRechargeOrderNo(), rechargeOrder.getTotalAmount());
        Map<String, Object> returnData = new HashMap<String, Object>();
        returnData.put("rechargeOrderNo", rechargeOrder.getRechargeOrderNo());
        returnData.put("orderInfo", orderInfo);
        returnData.put("rechargeAmount", rechargeOrder.getTotalAmount());
        returnData.put("payType", rechargeOrder.getPayType());
        returnData.put("payMethod", rechargeOrder.getPayMethod());
        return returnData;
    }

}
