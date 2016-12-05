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
     * Description: 获取用户充值订单列表
     * All Rights Reserved.
     *
     * @param
     * @return
     * @version 1.0  2016-11-10 23:06 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    ResponseVo<List<RechargeOrderVO>> getRechargeOrderList(Long userId) throws Exception;

    /**
     * Description: 获取用户充值订单列表，带分页
     * All Rights Reserved.
     *
     * @param
     * @return
     * @version 1.0  2016-11-17 20:42 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    ResponseVo<List<RechargeOrderVO>> getRechargeOrderList(Long userId, int start, int end) throws Exception;

    /**
     * Description: 分页查询充值订单列表，用于后台查询
     * All Rights Reserved.
     * @param
     * @return 
     * @version 1.0  2016-12-05 17:47 by wgh（guanhua.wang@pintuibao.cn）创建
     */ 
    ResponseVo<List<RechargeOrderVO>> getRechargeOrderListByPage(int pageNum, int pageSize, RechargeOrderQueryVO rechargeOrderQueryVO) throws Exception;

    /**
     * 获取线下充值详情
     *
     * @param rechargeOrderId 充值ID
     * @param rechargeOrderNo 充值订单号
     * @param userId          用户编号
     * @return
     */
    public ResponseVo<RechargeOrderVO> getRechargeOrderDetail(Long rechargeOrderId, String rechargeOrderNo, Long userId);
}

