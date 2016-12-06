package com.ptb.pay.enums;

/**
 * Description: 充值失败记录日志状态
 * All Rights Reserved.
 * @version 1.0  2016-11-30 17:13 by wgh（guanhua.wang@pintuibao.cn）创建
 */ 
public enum RechargeFailedLogStatusEnum {

    UNDEAL(0, "未处理"),
    DEAL(1, "已处理")
    ;

    int status;
    String desc;

    private RechargeFailedLogStatusEnum(int status, String desc){
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
