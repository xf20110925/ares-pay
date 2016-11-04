package com.ptb.pay.api.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ptb.common.errorcode.CommonErrorCode;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IPaymentApi;
import com.ptb.service.api.IBlockApi;
import com.ptb.service.api.ISystemConfigApi;
import com.ptb.utils.service.ReturnUtil;

import vo.PaymentVo;

/**
 * 支付api Description: All Rights Reserved.
 * 
 * @version 1.0 2016年11月2日 下午6:06:54 by 王冠华（guanhua.wang@pintuibao.cn）创建
 */
@Component("paymentApi")
public class PaymentApiImpl implements IPaymentApi {

	private static Logger logger = LoggerFactory.getLogger(PaymentApiImpl.class);

	/**
	 * 系统允许的支付方式，线上、线下
	 */
	private static final String SYSTEM_ALOW_PAYMENT = "system.alow.payment";

	@Autowired
	private IBlockApi blockApi;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ptb.pay.api.IPaymentApi#getPaymentList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseVo<List<PaymentVo>> getPaymentList(String deviceType) throws Exception {
		ResponseVo<String> systemAlowPaymentMethod = blockApi.getBlockValueByCode(SYSTEM_ALOW_PAYMENT);
		if (!"0".equals(systemAlowPaymentMethod.getCode()) || StringUtils.isBlank(systemAlowPaymentMethod.getData())) {
			return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_INERERROR.getCode(),
					CommonErrorCode.COMMMON_ERROR_INERERROR.getMessage());
		}
		try {
			List<PaymentVo> returnData = JSON.parseArray(systemAlowPaymentMethod.getData(), PaymentVo.class);
			return ReturnUtil.success(returnData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取支付方式失败", e);
			return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_INERERROR.getCode(),
					CommonErrorCode.COMMMON_ERROR_INERERROR.getMessage());
		}
	}
}
