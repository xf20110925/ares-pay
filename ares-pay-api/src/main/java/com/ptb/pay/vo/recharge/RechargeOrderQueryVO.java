package com.ptb.pay.vo.recharge;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Description: 充值订单查询VO
 * All Rights Reserved.
 *
 * @version 1.0  2016-12-01 18:49  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class RechargeOrderQueryVO  implements Serializable {

    private static final long serialVersionUID = -6077002763918956443L;
    private Long rechargeOrderId;
    private Integer payMethod;
    private Integer payType;
    private String rechargeOrderNo;
    private Long userId;
    private String userName;
    private Integer status;
    private Integer invoiceStatus;
    private Long invoiceId;
    private Date startTime;
    private Date endTime;
    private String verificationCode;
    private List<Long> rechargeOrderIds;

    public List<Long> getRechargeOrderIds() {
        return rechargeOrderIds;
    }

    public void setRechargeOrderIds(List<Long> rechargeOrderIds) {
        this.rechargeOrderIds = rechargeOrderIds;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Long getRechargeOrderId() {
        return rechargeOrderId;
    }

    public void setRechargeOrderId(Long rechargeOrderId) {
        this.rechargeOrderId = rechargeOrderId;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getRechargeOrderNo() {
        return rechargeOrderNo;
    }

    public void setRechargeOrderNo(String rechargeOrderNo) {
        this.rechargeOrderNo = rechargeOrderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
