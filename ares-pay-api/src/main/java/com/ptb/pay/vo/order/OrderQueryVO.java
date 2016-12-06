package com.ptb.pay.vo.order;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by MyThinkpad on 2016/12/6.
 */
public class OrderQueryVO implements Serializable {
    private String orderNo;

    private Integer orderStatus;

    private Integer sellerStatus;

    private Integer buyerStatus;

    private Date startTime;

    private Date endTime;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getSellerStatus() {
        return sellerStatus;
    }

    public void setSellerStatus(Integer sellerStatus) {
        this.sellerStatus = sellerStatus;
    }

    public Integer getBuyerStatus() {
        return buyerStatus;
    }

    public void setBuyerStatus(Integer buyerStatus) {
        this.buyerStatus = buyerStatus;
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
}
