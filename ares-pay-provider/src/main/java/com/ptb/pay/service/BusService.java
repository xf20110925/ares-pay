package com.ptb.pay.service;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.ptb.account.api.IAccountApi;
import com.ptb.account.vo.PtbAccountVo;
import com.ptb.account.vo.param.AccountRechargeParam;
import com.ptb.common.vo.ResponseVo;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.pay.model.vo.AccountRechargeParamMessageVO;
import com.ptb.utils.encrypt.SignUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
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

    private static Logger LOGGER = LoggerFactory.getLogger(BusService.class);

//    @PostConstruct
    private void initBus(){
        LOGGER.info("init bus start.............");
        bus = new KafkaBus(MESSAGE_RECHARGE_ERROR_RETRY_TOPIC);
        bus.start(false, 1);
        bus.addRecvListener((bus1, recvBus, message) -> {
            String jsonStr = new String(message);
            LOGGER.info("recieve message :" + message);
            if(StringUtils.isNotBlank(jsonStr)){
                AccountRechargeParamMessageVO messageVO = JSONObject.parseObject(jsonStr, AccountRechargeParamMessageVO.class);
                AccountRechargeParam rechargeParam = messageVO.getAccountRechargeParam();
                try {
                    //隐式加密
                    TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(rechargeParam), TreeMap.class);
                    String sign = SignUtil.getSignKey(toSign);
                    RpcContext.getContext().setAttachment("key", sign);
                    ResponseVo<PtbAccountVo> repsonseVO = accountApi.recharge(rechargeParam);
                    if (repsonseVO == null || !"0".equals(repsonseVO.getCode())) {
                        sendAccountRechargeRetryMessage(messageVO);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendAccountRechargeRetryMessage(messageVO);
                }
            }
        });
        LOGGER.info("init bus end..........");
    }

    /**
     * Description: 充值失败发送消息重试
     * All Rights Reserved.
     * @param
     * @return 
     * @version 1.0  2016-11-16 16:56 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    @Async
    public void sendAccountRechargeRetryMessage(AccountRechargeParamMessageVO messageVO) {
        String message = JSONObject.toJSONString(messageVO);
        if(messageVO.getSendTimes() >= MESSAGE_RETRY_MAX_TIMES){
           //todo 发送邮件，触发人工处理
            LOGGER.error("重试次数超过阀值，发送邮件触发人工处理！ message:" + message);
        } else {
            messageVO.setSendTimes(messageVO.getSendTimes() + 1);
            bus.send(new Message(MESSAGE_SRC, MESSAGE_RECHARGE_ERROR_RETRY_TOPIC, 0, "1.0.0", message));
        }
    }

    @PreDestroy
    private void destroyBus(){
        bus.showdown();
    }
}
