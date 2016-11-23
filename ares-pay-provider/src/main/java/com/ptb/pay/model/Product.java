package com.ptb.pay.model;

import java.util.Date;

public class Product {
    private Long ptbProductId;

    private String productName;

    private Integer productType;

    private Long price;

    private Date createTime;

    private Date lastUpdateTime;

    private Long ownerId;

    private Integer ownerType;

    private Integer status;

    private String description;

    private Integer dealNum;

    private Long relevantId;

    private String pmid;

    private Integer mediaType;

    public Long getPtbProductId() {
        return ptbProductId;
    }

    public void setPtbProductId(Long ptbProductId) {
        this.ptbProductId = ptbProductId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc == null ? null : desc.trim();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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