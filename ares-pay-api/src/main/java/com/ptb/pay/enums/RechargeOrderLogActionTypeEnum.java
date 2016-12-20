package com.ptb.pay.enums;

/**
 * Description: 充值失败记录日志状态
 * All Rights Reserved.
 * @version 1.0  2016-11-30 17:13 by wgh（guanhua.wang@pintuibao.cn）创建
 */ 
public enum RechargeOrderLogActionTypeEnum {

    CREATED(1, "用户创建"),
    PAYED(2, "在线支付到账"),
    OFFLINE_RECHARGE(3, "管理员线下确认充值"),
    SUBMIT_INVOICE(4, " 用户提交发票"),
    CONFIRM_INVOICE(5, "管理员确认已开发票并邮寄"),
    UPDATE_PROCESSING_AMOUNT(6, "系统更新手续费"),
    ;

    int actionType;
    String desc;

    private RechargeOrderLogActionTypeEnum(int actionType, String desc){
        this.actionType = actionType;
        this.desc = desc;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
