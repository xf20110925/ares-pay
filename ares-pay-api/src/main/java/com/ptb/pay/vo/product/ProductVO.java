package com.ptb.pay.vo.product;

import java.io.Serializable;

/**
 * Created by MyThinkpad on 2016/11/18.
 */
public class ProductVO implements Serializable {

    private Long productId;
    private String productName;
    private Integer productType;
    private Long price;
    private Long createTime;
    private Long updateTime;
    private Long ownerId;
    private Integer ownerType;
    private Integer status;
    private String desc;
    private Integer dealNum;
    private Long relevantId;
    private int deviceType;
    private String pmid;
    private Integer mediaType;

    public ProductVO(){}

    public ProductVO(long userId, int userType, String productName, int productType, long relevantId){
        this.productName = productName;
        this.productType = productType;
        this.ownerId = userId;
        this.ownerType = userType;
        Long time = System.currentTimeMillis();
        this.createTime = time;
        this.updateTime = time;
        this.status = ProductState.PRODUCT_OFF_SELL.getStatus();
        this.dealNum = 0;
        this.relevantId = relevantId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Integer ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getDealNum() {
        return dealNum;
    }

    public void setDealNum(Integer dealNum) {
        this.dealNum = dealNum;
    }

    public Long getRelevantId() {
        return relevantId;
    }

    public void setRelevantId(Long relevantId) {
        this.relevantId = relevantId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }
}
