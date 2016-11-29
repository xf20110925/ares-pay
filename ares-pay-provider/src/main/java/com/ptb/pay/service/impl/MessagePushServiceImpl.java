package com.ptb.pay.service.impl;

import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.enums.OrderActionEnum;
import com.ptb.pay.model.Order;
import com.ptb.pay.service.interfaces.IMessagePushService;
import com.ptb.service.api.IBaiduPushApi;
import enums.MessageTypeEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.param.PushMessageParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MyThinkpad on 2016/11/29.
 */

@Service
public class MessagePushServiceImpl implements IMessagePushService {
    private Logger logger = Logger.getLogger(MessagePushServiceImpl.class);

    @Autowired
    private IBaiduPushApi baiduPushApi;

    @Override
    public boolean pushOrderMessage(long userId, long toUserId, long orderId, OrderActionEnum orderActionEnum, DeviceTypeEnum deviceTypeEnum) {
        ResponseVo responseVo = null;

        String title = "订单状态更新", message = null;
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("orderId", orderId);

        if(orderActionEnum.getOrderAction() == OrderActionEnum.BUYER_SUBMIT_ORDER.getOrderAction()){
            message = "买家已拍下媒体，等待付款中";
        }else if(orderActionEnum.getOrderAction() == OrderActionEnum.BUYER_CANCAL_ORDER.getOrderAction()){
            message = "买家取消了订单，交易已关闭";
        }else if(orderActionEnum.getOrderAction() == OrderActionEnum.SALER_MODIFY_PRICE.getOrderAction()){
            message = "卖家已修改订单价格，请及时查看";
        }else if(orderActionEnum.getOrderAction() == OrderActionEnum.BUYER_COMPLETE.getOrderAction()){
            message = "买家已确认完成，收入已转入钱包余额";
        }else if(orderActionEnum.getOrderAction() == OrderActionEnum.SALER_COMPLETE.getOrderAction()){
            message = "卖家已确认投放完成，请及时确认";
        }else {
            logger.error("Unknown message OrderActionEnum");
            return false;
        }

        responseVo = baiduPushApi.pushMessage(
                generateMessageParam(toUserId, deviceTypeEnum, title, message, MessageTypeEnum.ORDER_TIP, keyMap));
        return responseVo.getCode().equals("0");

    }


    private PushMessageParam generateMessageParam(long userId, DeviceTypeEnum deviceTypeEnum, String title, String message, MessageTypeEnum messageTypeEnum, Map<String,Object> map){
        PushMessageParam param = new PushMessageParam();
        param.setUserId(userId);
        param.setDeviceType(deviceTypeEnum);
        param.setTitle(title);
        param.setMessage(message);
        param.setMessageType(messageTypeEnum.getMessageType());
        param.setContentParam(map);
        return param;
    }

}
