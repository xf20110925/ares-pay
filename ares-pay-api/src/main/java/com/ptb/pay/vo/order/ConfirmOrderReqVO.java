package com.ptb.pay.vo.order;

import com.ptb.pay.vo.BaseVO;

/**
 * Created by MyThinkpad on 2016/11/21.
 */
public class ConfirmOrderReqVO extends BaseVO{
    private long userId;
    private int  userType;
    private String password;
    private long orderId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}

