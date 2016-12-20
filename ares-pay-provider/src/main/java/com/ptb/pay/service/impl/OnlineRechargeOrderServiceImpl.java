package com.ptb.pay.service.impl;

import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.RechargeOrderInvoiceStatusEnum;
import com.ptb.common.enums.RechargeOrderStatusEnum;
import com.ptb.pay.enums.RechargeOrderLogActionTypeEnum;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.service.factory.OnlinePaymentServiceFactory;
import com.ptb.pay.service.interfaces.IOnlinePaymentService;
import com.ptb.pay.service.interfaces.IRechargeOrderLogService;
import com.ptb.pay.service.interfaces.IRechargeOrderService;
import com.ptb.pay.vo.recharge.RechargeOrderParamsVO;
import com.ptb.utils.tool.GenerateOrderNoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class OnlineRechargeOrderServiceImpl implements IRechargeOrderService {

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Autowired
    private IRechargeOrderLogService rechargeOrderLogService;

    @Override
    public RechargeOrder createRechargeOrder(RechargeOrderParamsVO paramsVO) throws Exception {

        String rechargeOrderNo = GenerateOrderNoUtil.createRechargeOrderNo(paramsVO.getDeviceType(), paramsVO.getPayMethod());
        if (StringUtils.isBlank(rechargeOrderNo)) {
            return null;
        }
        Date now = new Date();
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setCreateTime(now);
        rechargeOrder.setOrderNo(paramsVO.getOrderNo());
        rechargeOrder.setPayMethod(paramsVO.getPayMethod());
        rechargeOrder.setRechargeOrderNo(rechargeOrderNo);
        rechargeOrder.setStatus(RechargeOrderStatusEnum.unpay.getRechargeOrderStatus());
        rechargeOrder.setTotalAmount(paramsVO.getRechargeAmount());
        rechargeOrder.setUserId(paramsVO.getUserId());
        rechargeOrder.setPayType(paramsVO.getPayType());
        rechargeOrder.setInvoiceStatus(RechargeOrderInvoiceStatusEnum.noopen.getRechargeOrderInvoiceStatus());
        rechargeOrder.setDeviceType(paramsVO.getDeviceType());
        rechargeOrderMapper.insert(rechargeOrder);

        rechargeOrderLogService.saveUserOpLog(rechargeOrder.getRechargeOrderNo(),
                RechargeOrderLogActionTypeEnum.CREATED.getActionType(), null, rechargeOrder.getUserId());
        return rechargeOrder;
    }

    @Override
    public Map<String, Object> getReturnData(RechargeOrder rechargeOrder) throws Exception {
        IOnlinePaymentService onlinePaymentService = OnlinePaymentServiceFactory.createService(rechargeOrder.getPayType());
        String orderInfo = null;
        if (DeviceTypeEnum.PC.getDeviceType().equalsIgnoreCase(rechargeOrder.getDeviceType())) {
            orderInfo = onlinePaymentService.getPcPaymentInfo(rechargeOrder.getRechargeOrderNo(), rechargeOrder.getTotalAmount());
        } else {
            orderInfo = onlinePaymentService.getPaymentInfo(rechargeOrder.getRechargeOrderNo(), rechargeOrder.getTotalAmount());
        }
        Map<String, Object> returnData = new HashMap<String, Object>();
        returnData.put("rechargeOrderNo", rechargeOrder.getRechargeOrderNo());
        returnData.put("rechargeOrderId", rechargeOrder.getPtbRechargeOrderId());
        returnData.put("orderInfo", orderInfo);
        returnData.put("rechargeAmount", rechargeOrder.getTotalAmount());
        returnData.put("payType", rechargeOrder.getPayType());
        returnData.put("payMethod", rechargeOrder.getPayMethod());
        return returnData;
    }

}
