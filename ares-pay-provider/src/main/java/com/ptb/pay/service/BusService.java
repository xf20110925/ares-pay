package com.ptb.pay.service;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ptb.account.api.IAccountApi;
import com.ptb.account.vo.PtbAccountVo;
import com.ptb.account.vo.param.AccountRechargeParam;
import com.ptb.common.enums.PaymentMethodEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.pay.enums.RechargeFailedLogStatusEnum;
import com.ptb.pay.enums.RechargeOrderLogActionTypeEnum;
import com.ptb.pay.mapper.impl.RechargeFailedLogMapper;
import com.ptb.pay.model.RechargeFailedLog;
import com.ptb.pay.model.vo.AccountRechargeParamMessageVO;
import com.ptb.pay.service.interfaces.IRechargeOrderLogService;
import com.ptb.utils.encrypt.SignUtil;
import com.ptb.utils.tool.ShellUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.TreeMap;

/**
 * Description: 消息总线service
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-16 12:01  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Service
public class BusService {

    private static final String MESSAGE_SRC = "ares-pay";

    private static final String MESSAGE_RECHARGE_ERROR_RETRY_TOPIC = "payment_recharge_error_retry.topic";

    /**
     * 消息失败最大重试次数
     */
    private static final int MESSAGE_RETRY_MAX_TIMES = 10;

    private Bus bus;

    @Autowired
    private IAccountApi accountApi;

    @Autowired
    private RechargeFailedLogMapper rechargeFailedLogMapper;

    @Autowired
    private IRechargeOrderLogService rechargeOrderLogService;

    private static Logger LOGGER = LoggerFactory.getLogger(BusService.class);

    @PostConstruct
    private void initBus() {
        LOGGER.info("init bus start.............");
        bus = new KafkaBus(MESSAGE_RECHARGE_ERROR_RETRY_TOPIC);
        bus.start(false, 1);
        bus.addRecvListener((bus1, recvBus, message) -> {
            String jsonStr = new String(message);
            LOGGER.info("recieve message :" + jsonStr);
            if (StringUtils.isNotBlank(jsonStr)) {
                Message msg = JSONObject.parseObject(jsonStr, Message.class);
                AccountRechargeParamMessageVO messageVO = JSONObject.toJavaObject((JSONObject) msg.getBody(), AccountRechargeParamMessageVO.class);
                AccountRechargeParam rechargeParam = messageVO.getAccountRechargeParam();
                if (rechargeParam != null) {
                    try {
                        //隐式加密
                        TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(rechargeParam), TreeMap.class);
                        String sign = SignUtil.getSignKey(toSign);
                        RpcContext.getContext().setAttachment("key", sign);
                        ResponseVo<PtbAccountVo> repsonseVO = accountApi.recharge(rechargeParam);
                        if (repsonseVO == null || !"0".equals(repsonseVO.getCode())) {
                            sendAccountRechargeRetryMessage(messageVO);
                        } else {
                            if (rechargeParam.getPayMethod() == PaymentMethodEnum.offline.getPaymentMethod()) {
                                rechargeOrderLogService.saveAdminOpLog(rechargeParam.getOrderNo(),
                                        RechargeOrderLogActionTypeEnum.OFFLINE_RECHARGE.getActionType(), messageVO.getRemarks(), messageVO.getAdminId());
                            } else {
                                rechargeOrderLogService.saveUserOpLog(rechargeParam.getOrderNo(),
                                        RechargeOrderLogActionTypeEnum.PAYED.getActionType(), null, rechargeParam.getUserId());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendAccountRechargeRetryMessage(messageVO);
                    }
                }
            }
        });
        LOGGER.info("init bus end..........");
    }

    /**
     * Description: 充值失败发送消息重试
     * All Rights Reserved.
     *
     * @param
     * @return
     * @version 1.0  2016-11-16 16:56 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    @Async
    public void sendAccountRechargeRetryMessage(AccountRechargeParamMessageVO messageVO) {
        if (messageVO.getSendTimes() >= MESSAGE_RETRY_MAX_TIMES) {
            LOGGER.error("重试次数超过阀值，发送微信报警触发人工处理！ message:" + JSONObject.toJSONString(messageVO));
            AccountRechargeParam params = messageVO.getAccountRechargeParam();
            String rechargeOrderNo = params.getOrderNo();
            //发送微信报警
            ShellUtil.sendWeixinAlarm("充值失败", "充值失败,速处理！订单号：" + rechargeOrderNo);
            //记录日志,人工处理后要修改日志状态
            RechargeFailedLog log = new RechargeFailedLog();
            log.setCreateTime(new Date());
            log.setRechargeOrderNo(rechargeOrderNo);
            log.setRechargeParams(JSON.toJSONString(params));
            log.setStatus(RechargeFailedLogStatusEnum.UNDEAL.getStatus());
            log.setTotalAmount(params.getMoney());
            rechargeFailedLogMapper.insert(log);
        } else {
            int sendTimes = messageVO.getSendTimes();
            try {
                Thread.sleep(sendTimes * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageVO.setSendTimes(sendTimes + 1);
            bus.send(new Message(MESSAGE_SRC, MESSAGE_RECHARGE_ERROR_RETRY_TOPIC, 0, "1.0.0", messageVO));
        }
    }

    @PreDestroy
    private void destroyBus() {
        bus.showdown();
    }
}
