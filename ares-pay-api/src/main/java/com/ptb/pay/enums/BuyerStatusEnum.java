package com.ptb.pay.enums;

/**
 * Created by MyThinkpad on 2016/11/21.
 */
public enum BuyerStatusEnum {
    BUYER_STATUS_INIT(0, "初始状态"),
    BUYER_STATUS_PAY(1, "买家付款状态"),
    BUYER_STATUS_APPLY_REFUND(2, "买家申请退款状态"),
    BUYER_STATUS_CONFIRM(3, "买家确认完成状态"),
    BUYER_STATUS_CANCLE_REFUND(4, "买家取消申请退款状态"),
    BUYER_STATUS_CANCLE_ORDER(5, "买家取消订单");

    int status;
    String desc;

    private BuyerStatusEnum(int status, String desc){
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
