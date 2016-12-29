package com.ptb.pay.api;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.vo.recharge.*;

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
    public ResponseVo<Object> updateInvoiceStatus(RechargeOrderVO rechargeOrderVO, List<Long> rechargeOrderIds, Long invoiceId, Long adminId) throws Exception;

    /**
     * Description: 管理员后台进行线下充值
     * All Rights Reserved.
     * @param
     * @return 
     * @version 1.0  2016-12-07 18:34 by wgh（guanhua.wang@pintuibao.cn）创建
     */ 
    ResponseVo offlineRecharge(Long rechargeOrderId, Long rechargeAmount, Long adminId) throws Exception;

    /**
     * Description: 失败充值订单查询
     * All Rights Reserved.
     * @param
     * @return
     * @version 1.0  2016-12-23 15:24 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    ResponseVo<Object> getFailedRechargeOrderListByPage(int pageNum, int pageSize, FailedRechargeOrderQueryVO failedRechargeOrderQueryVO) throws Exception;

    /**
     * Description: 获取失败充值订单详情
     * All Rights Reserved.
     * @param 
     * @return 
     * @version 1.0  2016-12-26 10:58 by wgh（guanhua.wang@pintuibao.cn）创建
     */ 
    ResponseVo<RechargeFailedLogVO> getFailedRechargeOrder(Long failedRechargeOrderId);

    /**
     * Description: 处理失败的充值订单
     * All Rights Reserved.
     * @param
     * @return 
     * @version 1.0  2016-12-26 11:09 by wgh（guanhua.wang@pintuibao.cn）创建
     */ 
    ResponseVo rechargeFailedOrder(Long failedRechargeOrderId, Long adminId);

    /**
     * 批量更新手续费接口
     * @param payFee
     * @return
     */
    ResponseVo batchUpdatePayFee( List<Map<String, Object>> payFee);
}

