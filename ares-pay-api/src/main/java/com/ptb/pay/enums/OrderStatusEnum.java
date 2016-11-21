package com.ptb.pay.enums;

/**
 * Created by MyThinkpad on 2016/11/21.
 */
public enum OrderStatusEnum {
    ORDER_STATUS_NEW_DEAL(0, "新建状态"),
    ORDER_STATUS_DEALING(1, "进行中状态"),
    ORDER_STATUS_DEAL_OVER(2, "已完成状态"),
    ORDER_STATUS_DEAL_CLOSE(3, "已关闭状态"),
    ORDER_STATUS_DEAL_ALL(4, "全部状态");

    int status;
    String desc;

    private OrderStatusEnum(int status, String desc){
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
