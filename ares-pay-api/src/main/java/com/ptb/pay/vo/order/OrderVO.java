package com.ptb.pay.vo.order;

import com.ptb.pay.vo.product.ProductVO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by watson zhang on 2016/11/21.
 */
public class OrderVO implements Serializable{
    private Long ptbOrderId;

    private String orderNo;

    private Integer orderStatus;

    private Integer sellerStatus;

    private Integer buyerStatus;

    private Long originalPrice;

    private Long payablePrice;

    private Long sellerId;

    private Long buyerId;

    private Date createTime;

    private Date lastModifyTime;

    private String remarks;

    private Date payTime;

    private Long lastModifierId;

    private String button;

    private String desc;

    private List<ProductVO> productVOList;

    private List<OrderTimeAxis> timeAxises;

    public Long getPtbOrderId() {
        return ptbOrderId;
    }

    public void setPtbOrderId(Long ptbOrderId) {
        this.ptbOrderId = ptbOrderId;
    }

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

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Long getLastModifierId() {
        return lastModifierId;
    }

    public void setLastModifierId(Long lastModifierId) {
        this.lastModifierId = lastModifierId;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ProductVO> getProductVOList() {
        return productVOList;
    }

    public void setProductVOList(List<ProductVO> productVOList) {
        this.productVOList = productVOList;
    }

    public List<OrderTimeAxis> getTimeAxises() {
        return timeAxises;
    }

    public void setTimeAxises(List<OrderTimeAxis> timeAxises) {
        this.timeAxises = timeAxises;
    }
}
