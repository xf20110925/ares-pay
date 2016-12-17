package com.ptb.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.ptb.gaia.bus.Bus;
import com.ptb.gaia.bus.kafka.KafkaBus;
import com.ptb.gaia.bus.message.Message;
import com.ptb.pay.enums.OrderActionEnum;
import com.ptb.pay.model.vo.BuyerConfirmOrderVO;
import com.ptb.pay.model.vo.RetryMessageVO;
import com.ptb.pay.service.interfaces.IOrderService;
import com.ptb.utils.tool.ShellUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * Description: 消息总线service
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-16 12:01  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Service
public class OrderBusService {

    private static final String MESSAGE_SRC = "ares-pay";

    private static final String MESSAGE_ORDER_ERROR_RETRY_TOPIC = "order_error_retry_topic";

    /**
     * 消息失败最大重试次数
     */
    private static final int MESSAGE_RETRY_MAX_TIMES = 10;

    private Bus bus;

    private static Logger LOGGER = LoggerFactory.getLogger(OrderBusService.class);

    @Resource
    private IOrderService orderService;

    @PostConstruct
    private void initBus() {
        LOGGER.info("init bus start.............");
        bus = new KafkaBus(MESSAGE_ORDER_ERROR_RETRY_TOPIC);
        bus.start(false, 1);
        bus.addRecvListener((bus1, recvBus, message) -> {
            String jsonStr = new String(message);
            LOGGER.info("recieve message :" + jsonStr);
            if (StringUtils.isNotBlank(jsonStr)) {
                Message msg = JSONObject.parseObject(jsonStr, Message.class);
                RetryMessageVO messageVO = JSONObject.toJavaObject((JSONObject) msg.getBody(), RetryMessageVO.class);
                OrderActionEnum orderActionEnum = (OrderActionEnum) messageVO.getType();
                try {
                    if(orderActionEnum == OrderActionEnum.BUYER_COMPLETE) {
                        BuyerConfirmOrderVO confirmOrderVO = (BuyerConfirmOrderVO) messageVO.getBody();
                        if(!orderService.buyerConfirmOrder(confirmOrderVO.getUserId(), confirmOrderVO.getOrder()))
                            //失败重试
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

    @Async
    public void sendAccountRechargeRetryMessage(RetryMessageVO messageVO) {
        if (messageVO.getSendTimes() >= MESSAGE_RETRY_MAX_TIMES) {
            LOGGER.error("重试次数超过阀值，发送微信报警触发人工处理！ message:" + JSONObject.toJSONString(messageVO));
            //发送微信报警
            ShellUtil.sendWeixinAlarm(messageVO.getTitle(), messageVO.getMessage());
            //记录日志,人工处理后要修改日志状态
        } else {
            int sendTimes = messageVO.getSendTimes();
            try {
                Thread.sleep(sendTimes * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageVO.setSendTimes(sendTimes + 1);
            bus.send(new Message(MESSAGE_SRC, MESSAGE_ORDER_ERROR_RETRY_TOPIC, 0, "1.0.0", messageVO));
        }
    }

    @PreDestroy
    private void destroyBus() {
        bus.showdown();
    }
}
