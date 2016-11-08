package com.ptb.pay.api.impl;

import com.alibaba.fastjson.JSON;
import com.ptb.common.errorcode.CommonErrorCode;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IPaymentApi;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.service.IRechargeOrderService;
import com.ptb.pay.service.factory.RechargeOrderServiceFactory;
import com.ptb.service.api.IBlockApi;
import com.ptb.utils.service.ReturnUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vo.PaymentVO;
import vo.RechargeOrderParamsVO;

import java.util.List;
import java.util.Map;

/**
 * 支付api Description: All Rights Reserved.
 *
 * @version 1.0 2016年11月2日 下午6:06:54 by 王冠华（guanhua.wang@pintuibao.cn）创建
 */
@Component("paymentApi")
@Transactional
public class PaymentApiImpl implements IPaymentApi {


    private static Logger logger = LoggerFactory.getLogger(PaymentApiImpl.class);

    /**
     * 系统允许的支付方式，线上、线下
     */
    private static final String SYSTEM_ALOW_PAYMENT = "system.alow.payment";

    @Autowired
    private IBlockApi blockApi;

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;


    @SuppressWarnings("unchecked")
    @Override
    public ResponseVo<List<PaymentVO>> getPaymentList(String deviceType) throws Exception {
        ResponseVo<String> systemAlowPaymentMethod = blockApi.getBlockValueByCode(SYSTEM_ALOW_PAYMENT);
        if (!"0".equals(systemAlowPaymentMethod.getCode()) || StringUtils.isBlank(systemAlowPaymentMethod.getData())) {
            return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_INERERROR.getCode(),
                    CommonErrorCode.COMMMON_ERROR_INERERROR.getMessage());
        }
        try {
            List<PaymentVO> returnData = JSON.parseArray(systemAlowPaymentMethod.getData(), PaymentVO.class);
            return ReturnUtil.success(returnData);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取支付方式失败", e);
            return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_INERERROR.getCode(),
                    CommonErrorCode.COMMMON_ERROR_INERERROR.getMessage());
        }
    }

    @Override
    public ResponseVo<Map<String, Object>> createRechargeOrder(RechargeOrderParamsVO paramsVO) throws Exception {
        IRechargeOrderService rechargeOrderService = RechargeOrderServiceFactory.createService(paramsVO.getPayMethod());
        RechargeOrder rechargeOrder = rechargeOrderService.createRechargeOrder(paramsVO);
        return ReturnUtil.success(rechargeOrderService.getReturnData(rechargeOrder));
    }

}
