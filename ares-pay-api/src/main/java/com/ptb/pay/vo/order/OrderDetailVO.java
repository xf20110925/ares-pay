package com.ptb.pay.vo.order;

import java.io.Serializable;

/**
 * Created by watson zhang on 2016/11/20.
 */
public class OrderDetailVO implements Serializable{

    private long orderDetailId;
    private String orderNo;
    private long originalPrice;
    private long payAblePrice;
    private long productId;

    public OrderDetailVO(){}

    public OrderDetailVO(long orderDetailId, String orderNo, long originalPrice, long payAblePrice, long productId){
        this.orderDetailId = orderDetailId;
        this.orderNo = orderNo;
        this.originalPrice = originalPrice;
        this.payAblePrice = payAblePrice;
        this.productId = productId;
    }

    public long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public long getPayAblePrice() {
        return payAblePrice;
    }

    public void setPayAblePrice(long payAblePrice) {
        this.payAblePrice = payAblePrice;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
