package com.ptb.pay.model;

import java.util.Date;

public class RechargeFailedLog {
    private Long ptbRechargeFailedLogId;

    private String rechargeOrderNo;

    private Date createTime;

    private Integer status;

    private Long totalAmount;

    private String rechargeParams;

    public Long getPtbRechargeFailedLogId() {
        return ptbRechargeFailedLogId;
    }

    public void setPtbRechargeFailedLogId(Long ptbRechargeFailedLogId) {
        this.ptbRechargeFailedLogId = ptbRechargeFailedLogId;
    }

    public String getRechargeOrderNo() {
        return rechargeOrderNo;
    }

    public void setRechargeOrderNo(String rechargeOrderNo) {
        this.rechargeOrderNo = rechargeOrderNo == null ? null : rechargeOrderNo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRechargeParams() {
        return rechargeParams;
    }

    public void setRechargeParams(String rechargeParams) {
        this.rechargeParams = rechargeParams == null ? null : rechargeParams.trim();
    }
}