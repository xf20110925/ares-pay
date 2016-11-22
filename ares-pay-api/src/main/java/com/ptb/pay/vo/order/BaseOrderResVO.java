package com.ptb.pay.vo.order;

import java.io.Serializable;

/**
 * Created by watson zhang on 2016/11/22.
 */
public class BaseOrderResVO implements Serializable{

    String button;
    String desc;

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
