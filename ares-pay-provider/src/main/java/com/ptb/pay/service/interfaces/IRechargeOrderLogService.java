package com.ptb.pay.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Description: 充值订单操作日志service
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:09  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public interface IRechargeOrderLogService {

    /**
     * Description: 保存用户操作日志
     * All Rights Reserved.
     * @param
     * @return 
     * @version 1.0  2016-12-20 17:27 by wgh（guanhua.wang@pintuibao.cn）创建
     */ 
    boolean saveUserOpLog(String rechargeOrderNo, Integer actionType, String remarks, Long userId) throws Exception;

    /**
     * Description: 保存管理员操作日志
     * All Rights Reserved.
     * @param
     * @return 
     * @version 1.0  2016-12-20 17:28 by wgh（guanhua.wang@pintuibao.cn）创建
     */ 
    boolean saveAdminOpLog(String rechargeOrderNo, Integer actionType, String remarks, Long adminId) throws Exception;

    /**
     * 批量保存修改手续费日志
     * @param list
     */
    void batchSaveLog( List<Map<String, Object>> list);
}
