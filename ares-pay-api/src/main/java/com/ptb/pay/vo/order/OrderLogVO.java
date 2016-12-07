package com.ptb.pay.vo.order;

import java.io.Serializable;

/**
 * Created by watson zhang on 2016/12/7.
 */
public class OrderLogVO implements Serializable{
    long ptbOrderLogId;
    String orderNo;
    int actionType;
    long createTime;
    String remarks;
    long userId;
    int userType;

    public long getPtbOrderLogId() {
        return ptbOrderLogId;
    }

    public void setPtbOrderLogId(long ptbOrderLogId) {
        this.ptbOrderLogId = ptbOrderLogId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
}
