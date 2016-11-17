package com.ptb.pay.enums;

/**
 * Created by zuokui.fu on 2016/10/28.
 */
public enum ErrorCode {

    //通用错误码
    PAY_API_COMMMON_1000( "1000", "系统异常"),
    PAY_API_COMMMON_1001( "1001", "必填参数有空值"),
    PAY_API_COMMMON_1002( "1002", "金额不正确"),

    //订单相关错误码
    ORDER_API_5001( "5001", ""),
    ;

    private String code;
    private String message;

    ErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
