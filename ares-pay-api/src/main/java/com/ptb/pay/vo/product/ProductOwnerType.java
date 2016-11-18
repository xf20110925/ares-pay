package com.ptb.pay.vo.product;

/**
 * Created by MyThinkpad on 2016/11/18.
 */
public enum ProductOwnerType {

    USER_TYPE(1, "用户"),
    MANAGER_TYPE(2, "管理员");

    private int type;
    private String desc;

    ProductOwnerType(int type, String desc){
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
