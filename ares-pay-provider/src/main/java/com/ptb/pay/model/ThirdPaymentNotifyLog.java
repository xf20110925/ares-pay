package com.ptb.pay.model;

import java.util.Date;

public class ThirdPaymentNotifyLog {
    private Long pthThirdPaymentNotifyLogId;

    private String rechargeOrderNo;

    private Integer payType;

    private Date notifyTime;

    private String tradeStatus;

    private String notifyContent;

    public Long getPthThirdPaymentNotifyLogId() {
        return pthThirdPaymentNotifyLogId;
    }

    public void setPthThirdPaymentNotifyLogId(Long pthThirdPaymentNotifyLogId) {
        this.pthThirdPaymentNotifyLogId = pthThirdPaymentNotifyLogId;
    }

    public String getRechargeOrderNo() {
        return rechargeOrderNo;
    }

    public void setRechargeOrderNo(String rechargeOrderNo) {
        this.rechargeOrderNo = rechargeOrderNo == null ? null : rechargeOrderNo.trim();
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus == null ? null : tradeStatus.trim();
    }

    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent == null ? null : notifyContent.trim();
    }
}