package com.ptb.pay.enums;

/**
 * Created by zuokui.fu on 2016/10/28.
 */
public enum ErrorCode {

    //通用错误码
    PAY_API_COMMMON_1000( "1000", "系统异常"),
    PAY_API_COMMMON_1001( "1001", "必填参数有空值"),
    PAY_API_COMMMON_1002( "1002", "金额不正确"),

    //订单相关错误码5000-5999
    ORDER_API_5001( "5001", "登录用户ID与订单中的卖家ID不符"),
    ORDER_API_5002( "5002", "订单状态不正确"),
    ORDER_API_5003( "5003", "退款金额不正确"),
    ORDER_API_5004( "5004", "登录用户ID与订单中的买家ID不符"),

    //商品相关错误码6000-6999
    PRODUCT_API_REPEAT("6000", "相同用户商品重复"),
    PRODUCT_API_NO_EXISTS("6001", "商品不存在"),
    PRODUCT_API_PARAMETER_ERROR("6002", "参数错误"),
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
