package com.ptb.pay.model;

import java.util.Date;

public class RechargeOrder {
    private Long ptbRechargeOrderId;

    private String rechargeOrderNo;

    private Integer payType;

    private Long userId;

    private Integer payMethod;

    private Long totalAmount;

    private Long receiptAmount;

    private Long processingAmount;

    private Date createTime;

    private Date payTime;

    private Integer status;

    private String orderNo;

    private String verificationCode;

    private String deviceType;

    private Integer invoiceStatus;

    private Long invoiceId;

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

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
}