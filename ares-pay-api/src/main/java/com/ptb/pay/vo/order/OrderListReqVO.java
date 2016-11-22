package com.ptb.pay.vo.order;

import com.ptb.pay.vo.BaseVO;

import java.io.Serializable;

/**
 * Created by MyThinkpad on 2016/11/21.
 */
public class OrderListReqVO extends BaseVO implements Serializable{
    private long userId;
    private int  userType;
    private int  orderStatus;
    private int  start;
    private int  end;

    public OrderListReqVO(){}

    public OrderListReqVO(long userId, int userType, int orderStatus, int start, int end){
        this.userId = userId;
        this.userType = userType;
        this.orderStatus = orderStatus;
        this.start = start;
        this.end = end;
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

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
