package com.ptb.pay.service.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.ptb.account.api.IAccountApi;
import com.ptb.account.vo.PtbAccountVo;
import com.ptb.account.vo.param.AccountRechargeParam;
import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.PlatformEnum;
import com.ptb.common.enums.RechargeOrderStatusEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.enums.RechargeOrderLogActionTypeEnum;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.model.vo.AccountRechargeParamMessageVO;
import com.ptb.pay.service.BusService;
import com.ptb.pay.service.interfaces.IOfflinePaymentService;
import com.ptb.pay.service.interfaces.IRechargeOrderLogService;
import com.ptb.service.api.IBaiduPushApi;
import com.ptb.utils.encrypt.SignUtil;
import com.ptb.utils.tool.ChangeMoneyUtil;
import enums.MessageTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.param.PushMessageParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description:线下充值业务实现类
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:11  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Service("offlinePaymentService")
@Transactional
public class OfflinePaymentServiceImpl implements IOfflinePaymentService {

    private static Logger LOGGER = LoggerFactory.getLogger(OfflinePaymentServiceImpl.class);

    @Resource
    private IAccountApi accountApi;

    @Autowired
    private BusService busService;

    @Resource
    private IBaiduPushApi baiduPushApi;

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Autowired
    private IRechargeOrderLogService rechargeOrderLogService;

    /**
     * Description: 发送重试消息
     * All Rights Reserved.
     *
     * @param
     * @return
     * @version 1.0  2016-11-16 17:07 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    private void sendRetryMessage(AccountRechargeParam rechargeParam, Long adminId, String remarks) {
        AccountRechargeParamMessageVO messageVO = new AccountRechargeParamMessageVO();
        messageVO.setAccountRechargeParam(rechargeParam);
        messageVO.setAdminId(adminId);
        LOGGER.info("发送重试充值消息：" + JSONObject.toJSONString(messageVO));
        busService.sendAccountRechargeRetryMessage(messageVO);
    }

    @Override
    public boolean recharge(RechargeOrder rechargeOrder, Long adminId, Long rechargeAmount) throws Exception {

        String remarks = null;
        if(!rechargeOrder.getTotalAmount().equals(rechargeAmount)){
            remarks = "管理员修改线下充值金额，原金额：" + rechargeOrder.getTotalAmount() + "，充值金额：" + rechargeAmount;
            rechargeOrder.setTotalAmount(rechargeAmount);
        }

        AccountRechargeParam rechargeParam = new AccountRechargeParam();
        try {
            rechargeParam.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(rechargeOrder.getDeviceType()));
            rechargeParam.setMoney(rechargeOrder.getTotalAmount());
            rechargeParam.setUserId(rechargeOrder.getUserId());
            rechargeParam.setPayType(rechargeOrder.getPayType());
            rechargeParam.setPayMethod(rechargeOrder.getPayMethod());
            rechargeParam.setPlatformNo(PlatformEnum.xiaomi);
            rechargeParam.setOrderNo(rechargeOrder.getRechargeOrderNo());
            //隐式加密
            TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(rechargeParam), TreeMap.class);
            String sign = SignUtil.getSignKey(toSign);
            RpcContext.getContext().setAttachment("key", sign);

            ResponseVo<PtbAccountVo> repsonseVO = accountApi.recharge(rechargeParam);
            if (repsonseVO == null || !"0".equals(repsonseVO.getCode())) {
                sendRetryMessage(rechargeParam, adminId, remarks);
            } else {
                LOGGER.info("充值订单号：" + rechargeOrder.getRechargeOrderNo() + "充值成功!");
                try {
                    //推送消息
                    PushMessageParam param = new PushMessageParam();
                    param.setUserId(rechargeOrder.getUserId());
                    param.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(rechargeOrder.getDeviceType()));
                    param.setTitle("充值成功（线下打款）");
                    param.setMessage("线下打款金额" + ChangeMoneyUtil.fromFenToYuan(rechargeOrder.getTotalAmount()) + "元，审核已通过，充值金额已自动转入钱包余额");
                    param.setMessageType(MessageTypeEnum.OFFLINE_RECHARGE.getMessageType());
                    Map<String, Object> keyMap = new HashMap<>();
                    keyMap.put("id", rechargeOrder.getPtbRechargeOrderId());
                    param.setContentParam( keyMap);
                    param.setNeedSaveMessage( true);
                    param.setNeedPushMessage( true);
                    baiduPushApi.pushMessage(param);
                }catch (Exception e){
                    LOGGER.error( "线下充值消息推送失败。errorMsg:{}", e.getMessage());
                }
                rechargeOrderLogService.saveAdminOpLog(rechargeParam.getOrderNo(),
                        RechargeOrderLogActionTypeEnum.OFFLINE_RECHARGE.getActionType(), remarks, adminId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendRetryMessage(rechargeParam, adminId, remarks);
            LOGGER.error("线下充值失败，放入消息队列重试,params:" + JSONObject.toJSONString(rechargeParam));
        } finally {
            rechargeOrder.setStatus(RechargeOrderStatusEnum.paid.getRechargeOrderStatus());
            rechargeOrder.setPayTime(new Date());
            rechargeOrderMapper.updateByPrimaryKey(rechargeOrder);
        }

        return true;
    }
}
