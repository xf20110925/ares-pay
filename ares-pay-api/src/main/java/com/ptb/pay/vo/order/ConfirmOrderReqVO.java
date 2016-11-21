package com.ptb.pay.vo.order;

import com.ptb.pay.vo.BaseVO;

import java.io.Serializable;

/**
 * Created by MyThinkpad on 2016/11/21.
 */
public class ConfirmOrderReqVO extends BaseVO implements Serializable{
    private long userId;
    private int  userType;
    private String password;
    private long orderId;

    public ConfirmOrderReqVO(){}

    public ConfirmOrderReqVO(long userId, int userType, String password, long orderId){
        this.userId = userId;
        this.userType = userType;
        this.password = password;
        this.orderId = orderId;
    }

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

