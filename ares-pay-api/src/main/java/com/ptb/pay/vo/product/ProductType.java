package com.ptb.pay.vo.product;

/**
 * Created by MyThinkpad on 2016/11/18.
 */
public enum ProductType {
    MEDIA_SERVICE_TYPE(1, "媒体服务");

    private int type;
    private String desc;

    ProductType(int type, String desc){
        this.type = type;
        this.desc = desc;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
