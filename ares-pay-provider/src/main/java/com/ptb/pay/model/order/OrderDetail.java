package com.ptb.pay.model.order;

public class OrderDetail {
    private Long ptbOrderDetailId;

    private String orderNo;

    private Long originalPrice;

    private Long payablePrice;

    private Long productId;

    public Long getPtbOrderDetailId() {
        return ptbOrderDetailId;
    }

    public void setPtbOrderDetailId(Long ptbOrderDetailId) {
        this.ptbOrderDetailId = ptbOrderDetailId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getPayablePrice() {
        return payablePrice;
    }

    public void setPayablePrice(Long payablePrice) {
        this.payablePrice = payablePrice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}