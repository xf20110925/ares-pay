package com.ptb.pay.service.interfaces;

import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.vo.RechargeOrderParamsVO;

import java.util.Map;

/**
 * Description: 充值订单接口
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-07 19:41  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public interface IRechargeOrderService {

    /**
     * Description: 创建充值订单
     * All Rights Reserved.
     * @param
     * @return
     * @version 1.0  2016-11-07 19:42 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    RechargeOrder createRechargeOrder(RechargeOrderParamsVO paramsVO) throws Exception;

    /**
     * Description: 获取返回客户端的数据
     * All Rights Reserved.
     * @param
     * @return
     * @version 1.0  2016-11-08 11:30 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    Map<String, Object> getReturnData(RechargeOrder rechargeOrder) throws Exception;


}
