package com.ptb.pay.api;

import java.util.List;

import com.ptb.common.vo.ResponseVo;

import vo.PaymentVo;

/**
 * 支付api Description: All Rights Reserved.
 * 
 * @version 1.0 2016年11月2日 下午5:56:12 by 王冠华（guanhua.wang@pintuibao.cn）创建
 */
public interface IPaymentApi {

	/**
	 * 获取系统允许的支付方式
	 * Description: 
	 * @Version1.0 2016年11月3日 下午7:46:53 by 王冠华（guanhua.wang@pintuibao.cn）创建
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public ResponseVo<List<PaymentVo>> getPaymentList(String deviceType) throws Exception;
}
