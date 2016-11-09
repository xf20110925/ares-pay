package com.ptb.pay.api;

import com.ptb.common.vo.ResponseVo;
import vo.CheckPayResultVO;
import vo.PaymentVO;
import vo.RechargeOrderParamsVO;

import java.util.List;
import java.util.Map;

/**
 * 支付api Description: All Rights Reserved.
 *
 * @version 1.0 2016年11月2日 下午5:56:12 by 王冠华（guanhua.wang@pintuibao.cn）创建
 */
public interface IPaymentApi {

    /**
     * 获取系统允许的支付方式
     * Description:
     *
     * @param deviceType
     * @return
     * @throws Exception
     * @Version1.0 2016年11月3日 下午7:46:53 by 王冠华（guanhua.wang@pintuibao.cn）创建
     */
    ResponseVo<List<PaymentVO>> getPaymentList(String deviceType) throws Exception;

    /**
     * 创建充值单
     * Description:
     * All Rights Reserved.
     *
     * @param
     * @return
     * @version 1.0  2016-11-07 19:29 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    ResponseVo<Map<String, Object>> createRechargeOrder(RechargeOrderParamsVO paramsVO) throws Exception;

    /**
     * Description: 校验在线支付结果，如果成功直接充值
     * All Rights Reserved.
     *
     * @param
     * @return
     * @version 1.0  2016-11-09 10:11 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    ResponseVo<CheckPayResultVO> checkOnlinePayResult(RechargeOrderParamsVO paramsVO) throws Exception;

    /**
     * Description:  在线支付异步通知
     * All Rights Reserved.
     * @param
     * @return
     * @version 1.0  2016-11-09 17:16 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    public ResponseVo onlinePaymentNotify() throws Exception;
}

