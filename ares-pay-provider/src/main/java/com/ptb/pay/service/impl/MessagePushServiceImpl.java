package com.ptb.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.enums.OrderActionEnum;
import com.ptb.pay.enums.UserTypeEnum;
import com.ptb.pay.model.Order;
import com.ptb.pay.service.interfaces.IMessagePushService;
import com.ptb.service.api.IBaiduPushApi;
import enums.MessageTypeEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.param.PushMessageParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MyThinkpad on 2016/11/29.
 */

@Service
public class MessagePushServiceImpl implements IMessagePushService {
    private Logger logger = Logger.getLogger(MessagePushServiceImpl.class);

    @Resource
    private IBaiduPushApi baiduPushApi;

    @Override
    public boolean pushOrderMessage(long userId, long toUserId, long orderId, OrderActionEnum orderActionEnum, DeviceTypeEnum deviceTypeEnum) {
        ResponseVo responseVo = null;

        String title = "订单状态更新", message = null;
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("id", orderId);


        if(orderActionEnum.getOrderAction() == OrderActionEnum.BUYER_SUBMIT_ORDER.getOrderAction()){
            message = "买家已拍下媒体，等待付款中";
            keyMap.put("userType", UserTypeEnum.USER_IS_SELLER.getUserType());
        }else if(orderActionEnum.getOrderAction() == OrderActionEnum.BUYER_CANCAL_ORDER.getOrderAction()){
            message = "买家取消了订单，交易已关闭";
            keyMap.put("userType", UserTypeEnum.USER_IS_SELLER.getUserType());
        }else if(orderActionEnum.getOrderAction() == OrderActionEnum.SALER_MODIFY_PRICE.getOrderAction()){
            message = "卖家已修改订单价格，请及时查看";
            keyMap.put("userType", UserTypeEnum.USER_IS_BUYER.getUserType());
        }else if(orderActionEnum.getOrderAction() == OrderActionEnum.BUYER_COMPLETE.getOrderAction()){
            message = "买家已确认完成，收入已转入钱包余额";
            keyMap.put("userType", UserTypeEnum.USER_IS_SELLER.getUserType());
        }else if(orderActionEnum.getOrderAction() == OrderActionEnum.SALER_COMPLETE.getOrderAction()){
            message = "卖家已确认投放完成，请及时确认";
            keyMap.put("userType", UserTypeEnum.USER_IS_BUYER.getUserType());
        }else if(orderActionEnum.getOrderAction() == OrderActionEnum.BUYER_APPLY_REFUND.getOrderAction()){
            message = "买家发起了退款申请，请及时处理";
            keyMap.put("userType", UserTypeEnum.USER_IS_SELLER.getUserType());
        }else if(orderActionEnum.getOrderAction() == OrderActionEnum.SALER_AGREE_REFUND.getOrderAction()){
            message = "卖家已同意退款申请，退款金额已自动转入钱包余额";
            keyMap.put("userType", UserTypeEnum.USER_IS_BUYER.getUserType());
        }else {
            logger.error("Unknown message OrderActionEnum");
            return false;
        }
        PushMessageParam param = null;
        try {
            param = generateMessageParam(toUserId, userId != toUserId?null:deviceTypeEnum, title, message, MessageTypeEnum.ORDER_TIP, keyMap);
            param.setNeedSaveMessage( true);
            param.setNeedPushMessage( true);
            responseVo = baiduPushApi.pushMessage(param);
        }catch (Exception ee){
            logger.error("push order message error args:" + JSON.toJSONString(param) + " error:" + ee.getMessage());
            return false;
        }
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
