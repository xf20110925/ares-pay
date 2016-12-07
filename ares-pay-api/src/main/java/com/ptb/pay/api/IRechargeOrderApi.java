package com.ptb.pay.api;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.vo.recharge.RechargeOrderParamsVO;
import com.ptb.pay.vo.recharge.RechargeOrderQueryVO;
import com.ptb.pay.vo.recharge.RechargeOrderVO;

import java.util.List;
import java.util.Map;

/**
 * Description: 充值订单API
 * All Rights Reserved.
 *
 * @version 1.0 2016年11月2日 下午5:56:12 by 王冠华（guanhua.wang@pintuibao.cn）创建
 */
public interface IRechargeOrderApi {

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
     * Description: 分页查询充值订单列表，用于后台查询
     * All Rights Reserved.
     * @param
     * @return 
     * @version 1.0  2016-12-05 17:47 by wgh（guanhua.wang@pintuibao.cn）创建
     */ 
    ResponseVo<Object> getRechargeOrderListByPage(int pageNum, int pageSize, RechargeOrderQueryVO rechargeOrderQueryVO) throws Exception;

    /**
     * Description: 分页查询充值订单列表，用于后台查询，可以控制是否查总数
     * All Rights Reserved.
     * @param
     * @return 
     * @version 1.0  2016-12-06 14:53 by wgh（guanhua.wang@pintuibao.cn）创建
     */ 
    ResponseVo<Object> getRechargeOrderListByPage(int pageNum, int pageSize, RechargeOrderQueryVO rechargeOrderQueryVO, boolean count) throws Exception;
    /**
     * 获取线下充值详情
     *
     * @param rechargeOrderId 充值ID
     * @param rechargeOrderNo 充值订单号
     * @param userId          用户编号
     * @return
     */
    public ResponseVo<RechargeOrderVO> getRechargeOrderDetail(Long rechargeOrderId, String rechargeOrderNo, Long userId);

    /**
     * Description: 更新发票状态
     * All Rights Reserved.
     * @param
     * @return 
     * @version 1.0  2016-12-06 19:23 by wgh（guanhua.wang@pintuibao.cn）创建
     */ 
    public ResponseVo<Object> updateInvoiceStatus(RechargeOrderVO rechargeOrderVO, List<Long> rechargeOrderIds) throws Exception;
}

