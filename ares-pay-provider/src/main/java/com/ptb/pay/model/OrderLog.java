package com.ptb.pay.model;

import java.util.Date;

public class OrderLog {
    private Long ptbOrderLogId;

    private String orderNo;

    private Integer actionType;

    private Date createTime;

    private String remarks;

    private Long userId;

    private Integer userType;

    public Long getPtbOrderLogId() {
        return ptbOrderLogId;
    }

    public void setPtbOrderLogId(Long ptbOrderLogId) {
        this.ptbOrderLogId = ptbOrderLogId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}