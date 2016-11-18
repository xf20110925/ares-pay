package com.ptb.pay.vo.product;

/**
 * Created by MyThinkpad on 2016/11/18.
 */
public enum ProductState {
    PRODUCT_OFF_SELL( 0, "已下架"),
    PRODUCT_ON_SELL( 1, "已上架"),
    PRODUCT_ON_OFF_SELL(2, "所有商品")
    ;

    private int status;
    private String detail;

    ProductState(int status, String detail){
        this.status = status;
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
