package com.ptb.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ptb.common.enums.OnlinePaymentTypeEnum;
import com.ptb.pay.mapper.impl.ThirdPaymentNotifyLogMapper;
import com.ptb.pay.model.ThirdPaymentNotifyLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Description:
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-14 20:03  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Service
public class ThirdPaymentNotifyLogService {

    private static Logger LOGGER = LoggerFactory.getLogger(ThirdPaymentNotifyLogService.class);

    @Autowired
    private ThirdPaymentNotifyLogMapper thirdPaymentNotifyLogMapper;

    @Async
    public void asynSaveAlipayNotifyLog(Map<String, String> params) throws Exception{

        ThirdPaymentNotifyLog log = new ThirdPaymentNotifyLog();
        try {
            log.setRechargeOrderNo(params.get("out_trade_no"));
            log.setNotifyContent(JSON.toJSONString(params));
            log.setNotifyTime(new Date());
            log.setPayType(OnlinePaymentTypeEnum.ALIPAY.getPaymentTypeId());
            log.setTradeStatus(params.get("trade_status"));
            thirdPaymentNotifyLogMapper.insert(log);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("保存支付宝notify日志失败，log：" + JSONObject.toJSONString(log), e);
        }

    }
}
