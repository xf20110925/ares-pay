package com.ptb.pay.model.vo;

import com.ptb.pay.model.Order;

/**
 * Created by MyThinkpad on 2016/12/17.
 */
public class BuyerConfirmOrderVO {
    private long userId;
    private Order order;

    public BuyerConfirmOrderVO(long userId, Order order) {
        this.userId = userId;
        this.order = order;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
