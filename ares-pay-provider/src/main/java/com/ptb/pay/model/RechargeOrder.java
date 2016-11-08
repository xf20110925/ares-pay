package com.ptb.pay.model;

import java.util.Date;

public class RechargeOrder {
    private Long ptbRechargeOrderId;

    private String rechargeOrderNo;

    private int payType;

    private Long userId;

    private int payMethod;

    private Long totalAmount;

    private Long receiptAmount;

    private Long processingAmount;

    private Date createTime;

    private Date payTime;

    private int status;

    private String orderNo;

    private String verificationCode;

    public Long getPtbRechargeOrderId() {
        return ptbRechargeOrderId;
    }

    public void setPtbRechargeOrderId(Long ptbRechargeOrderId) {
        this.ptbRechargeOrderId = ptbRechargeOrderId;
    }

    public String getRechargeOrderNo() {
        return rechargeOrderNo;
    }

    public void setRechargeOrderNo(String rechargeOrderNo) {
        this.rechargeOrderNo = rechargeOrderNo == null ? null : rechargeOrderNo.trim();
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(Long receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public Long getProcessingAmount() {
        return processingAmount;
    }

    public void setProcessingAmount(Long processingAmount) {
        this.processingAmount = processingAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode == null ? null : verificationCode.trim();
    }
}