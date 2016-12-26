package com.ptb.pay.vo.recharge;

import java.util.Date;

public class RechargeFailedLogVO {

    private Long ptbRechargeFailedLogId;

    private String rechargeOrderNo;

    private Date createTime;

    private Integer status;

    private Long totalAmount;

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

}