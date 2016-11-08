package com.ptb.pay.service.impl;

import com.ptb.common.enums.RechargeOrderNoStatusEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.service.IRechargeOrderService;
import com.ptb.service.api.ISystemConfigApi;
import com.ptb.utils.tool.GenerateOrderNoUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vo.RechargeOrderParamsVO;

import java.util.*;

/**
 * Description: 线下充值接口
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-07 19:45  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Service
@Transactional
public class OfflineRechargeOrderServiceImpl implements IRechargeOrderService {

    /**
     * 银行名称
     */
    private static final String SYSTEM_CONFIG_BANKNAME = "offline.recharge.bankname";

    /**
     * 开户行
     */
    private static final String SYSTEM_CONFIG_OPENACCOUNT_BANKNAME = "offline.recharge.openAccountBankName";

    /**
     * 开户名称
     */
    private static final String SYSTEM_CONFIG_OPENACCOUNT_USERNAME = "offline.recharge.openAccountUserName";

    /**
     * 开户账号
     */
    private static final String SYSTEM_CONFIG_OPENACCOUNT_USERNUM = "offline.recharge.openAccountUserNum";


    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Autowired
    private ISystemConfigApi systemConfigApi;

    @Override
    public RechargeOrder createRechargeOrder(RechargeOrderParamsVO paramsVO) throws Exception {
        Date now = new Date();
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setCreateTime(now);
        rechargeOrder.setOrderNo(paramsVO.getOrderNo());
        rechargeOrder.setPayMethod(paramsVO.getPayMethod());
        rechargeOrder.setRechargeOrderNo(GenerateOrderNoUtil.createRechargeOrderNo(paramsVO.getDeviceType(), paramsVO.getPayMethod()));
        rechargeOrder.setStatus(RechargeOrderNoStatusEnum.unpay.getRechargeOrderNoStatus());
        rechargeOrder.setTotalAmount(paramsVO.getRechargeAmount());
        rechargeOrder.setVerificationCode(RandomStringUtils.randomNumeric(6));
        rechargeOrder.setUserId(paramsVO.getUserId());
        rechargeOrderMapper.insert(rechargeOrder);
        return rechargeOrder;
    }

    @Override
    public Map<String, Object> getReturnData(RechargeOrder rechargeOrder) throws Exception {

        Map<String, Object> returnData = new HashMap<String, Object>();
        returnData.put("rechargeOrderNo", rechargeOrder.getRechargeOrderNo());
        returnData.put("verificationCode", rechargeOrder.getVerificationCode());
        returnData.put("rechargeAmount", rechargeOrder.getTotalAmount());
        returnData.put("payMethod", rechargeOrder.getPayMethod());
        Map<String, String> returnMap = getOpenBankInfo();
        if(returnMap != null){
            returnData.put("bankName", returnMap.get(SYSTEM_CONFIG_BANKNAME));
            returnData.put("openAccountBankName", returnMap.get(SYSTEM_CONFIG_OPENACCOUNT_BANKNAME));
            returnData.put("openAccountUserName", returnMap.get(SYSTEM_CONFIG_OPENACCOUNT_USERNAME));
            returnData.put("openAccountUserNum", returnMap.get(SYSTEM_CONFIG_OPENACCOUNT_USERNUM));
        }
        return returnData;
    }

    /**
     * Description: 获取我方开户行信息
     * All Rights Reserved.
     * @param
     * @return
     * @version 1.0  2016-11-08 11:17 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    public Map<String, String> getOpenBankInfo() {
        List<String> params = new ArrayList<String>();
        params.add(SYSTEM_CONFIG_BANKNAME);
        params.add(SYSTEM_CONFIG_OPENACCOUNT_BANKNAME);
        params.add(SYSTEM_CONFIG_OPENACCOUNT_USERNAME);
        params.add(SYSTEM_CONFIG_OPENACCOUNT_USERNUM);
        ResponseVo<Map<String, String>> result = systemConfigApi.getConfigs(params);
        if (result != null && !CollectionUtils.isEmpty(result.getData())) {
            return result.getData();
        }
        return null;
    }

}
