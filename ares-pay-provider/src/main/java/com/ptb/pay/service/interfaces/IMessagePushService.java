package com.ptb.pay.service.interfaces;

import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.pay.enums.OrderActionEnum;
import com.ptb.pay.model.Order;

/**
 * Created by MyThinkpad on 2016/11/29.
 */
public interface IMessagePushService {

    /**
     * 订单状态变更 消息推送
     * @param userId 调用该接口的用户ID
     * @param toUserId 接收消息用户ID
     * @param orderId 订单ID
     * @param orderActionEnum 状态变更动作
     * @param deviceTypeEnum 设备类型
     * @return
     */
    public boolean pushOrderMessage(long userId, long toUserId, long orderId, OrderActionEnum orderActionEnum, DeviceTypeEnum deviceTypeEnum);
}
