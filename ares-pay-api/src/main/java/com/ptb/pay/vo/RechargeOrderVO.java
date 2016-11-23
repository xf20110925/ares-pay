package com.ptb.pay.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 充值订单VO
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-07 18:35  by wgh（guanhua.wang@pIntegeruibao.cn）创建
 */
public class RechargeOrderVO implements Serializable{

    private static final long serialVersionUID = -2720046974034786258L;
    private Long ptbRechargeOrderId;
    private Long orderId;
    private Long rechargeAmount;
    private Integer payMethod;
    private Integer payType;
    private String rechargeOrderNo;
    private String deviceType;
    private Long userId;
    private Integer status;
    private Date createTime;
    private Date payTime;
    private String verificationCode;

    public Long getPtbRechargeOrderId() {
        return ptbRechargeOrderId;
    }

    public void setPtbRechargeOrderId(Long ptbRechargeOrderId) {
        this.ptbRechargeOrderId = ptbRechargeOrderId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Long rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
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

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
