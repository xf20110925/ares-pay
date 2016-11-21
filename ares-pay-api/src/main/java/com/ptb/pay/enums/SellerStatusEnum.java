package com.ptb.pay.enums;

/**
 * Created by MyThinkpad on 2016/11/21.
 */
public enum SellerStatusEnum {
    SELLER_STATUS_INIT(0, "初始状态"),
    SELLER_STATUS_CONFIRM(1,"卖家确认完成状态"),
    SELLER_STATUS_AGREE_REFUND(2, "卖家同意退款状态");

    int status;
    String desc;

    private SellerStatusEnum(int status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
