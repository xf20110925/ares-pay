package com.ptb.pay.enums;

/**
 * Created by zuokui.fu on 2016/11/17.
 */
public enum OrderActionEnum {

    BUYER_PAY( 1, "买家支付"),
    BUYER_APPLY_REFUND( 2, "买家申请退款"),
    BUYER_CANCEL_REFUND( 3, "买家取消申请退款"),
    BUYER_COMPLETE( 4, "买家确认完成"),
    SALER_COMPLETE( 5, "卖家确认完成"),
    SALER_AGREE_REFUND( 6, "卖家同意退款"),
    BUYER_CANCAL_ORDER( 7, "买家取消订单"),
    BUYER_SUBMIT_ORDER( 8, "买家提交订单"),
    SALER_MODIFY_PRICE( 9, "卖家修改价格"),
    ADMIN_COMPLETE_ORDER(10, "管理员强制完成交易"),
    ADMIN_CANCEL_ORDER(11, "管理员强制退款"),
    ;

    private OrderActionEnum( int orderAction, String desc){
        this.orderAction = orderAction;
        this.desc = desc;
    }

    private int orderAction;
    private String desc;

    public int getOrderAction() {
        return orderAction;
    }

    public void setOrderAction(int orderAction) {
        this.orderAction = orderAction;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static OrderActionEnum getOrderActionInfo(int orderAction){

        OrderActionEnum orderActionEnum = null;
        switch (orderAction){
            case 1:
                orderActionEnum = OrderActionEnum.BUYER_PAY;
                break;
            case 2:
                orderActionEnum = OrderActionEnum.BUYER_APPLY_REFUND;
                break;
            case 3:
                orderActionEnum = OrderActionEnum.BUYER_CANCEL_REFUND;
                break;
            case 4:
                orderActionEnum = OrderActionEnum.BUYER_COMPLETE;
                break;
            case 5:
                orderActionEnum = OrderActionEnum.SALER_COMPLETE;
                break;
            case 6:
                orderActionEnum = OrderActionEnum.SALER_AGREE_REFUND;
                break;
            case 7:
                orderActionEnum = OrderActionEnum.BUYER_CANCAL_ORDER;
                break;
            case 8:
                orderActionEnum = OrderActionEnum.BUYER_SUBMIT_ORDER;
                break;
            case 9:
                orderActionEnum = OrderActionEnum.SALER_MODIFY_PRICE;
                break;
            case 10:
                orderActionEnum = OrderActionEnum.ADMIN_COMPLETE_ORDER;
                break;
            case 11:
                orderActionEnum = OrderActionEnum.ADMIN_CANCEL_ORDER;
                break;
            default:
                break;
        }
        return orderActionEnum;
    }
}
