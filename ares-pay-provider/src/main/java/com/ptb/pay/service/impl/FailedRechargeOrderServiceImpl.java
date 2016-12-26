package com.ptb.pay.service.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.ptb.account.api.IAccountApi;
import com.ptb.account.vo.PtbAccountVo;
import com.ptb.account.vo.param.AccountRechargeParam;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.enums.RechargeFailedLogStatusEnum;
import com.ptb.pay.enums.RechargeOrderLogActionTypeEnum;
import com.ptb.pay.mapper.impl.RechargeFailedLogMapper;
import com.ptb.pay.model.RechargeFailedLog;
import com.ptb.pay.service.interfaces.IFailedRechargeOrderService;
import com.ptb.pay.service.interfaces.IRechargeOrderLogService;
import com.ptb.utils.encrypt.SignUtil;
import com.ptb.utils.tool.ShellUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.TreeMap;

/**
 * Description:线下充值业务实现类
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:11  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Service("offlinePaymentService")
@Transactional
public class FailedRechargeOrderServiceImpl implements IFailedRechargeOrderService {

    private static Logger LOGGER = LoggerFactory.getLogger(FailedRechargeOrderServiceImpl.class);

    @Resource
    private IAccountApi accountApi;

    @Autowired
    private RechargeFailedLogMapper rechargeFailedLogMapper;

    @Autowired
    private IRechargeOrderLogService rechargeOrderLogService;


    @Override
    public boolean recharge(RechargeFailedLog rechargeFailedLog, Long adminId) throws Exception {

        AccountRechargeParam rechargeParam = null;
        try {
            rechargeParam = JSONObject.parseObject(rechargeFailedLog.getRechargeParams(), AccountRechargeParam.class);
            //隐式加密
            TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(rechargeParam), TreeMap.class);
            String sign = SignUtil.getSignKey(toSign);
            RpcContext.getContext().setAttachment("key", sign);

            ResponseVo<PtbAccountVo> repsonseVO = accountApi.recharge(rechargeParam);
            if (repsonseVO == null || !"0".equals(repsonseVO.getCode())) {
                return false;
            } else {
                LOGGER.info("充值订单号：" + rechargeParam.getOrderNo() + "补单成功!");
                rechargeOrderLogService.saveAdminOpLog(rechargeParam.getOrderNo(),
                        RechargeOrderLogActionTypeEnum.ADMIN_DEAL_FAILED.getActionType(), null, adminId);
                rechargeFailedLog.setStatus(RechargeFailedLogStatusEnum.DEAL.getStatus());
                rechargeFailedLogMapper.updateByPrimaryKey(rechargeFailedLog);
                //发送微信报警
                ShellUtil.sendWeixinAlarm("补单成功", "充值订单号：" + rechargeParam.getOrderNo() + "，补单成功！");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }
}
