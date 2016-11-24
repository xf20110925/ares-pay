package com.ptb.pay.vo.order;

import java.io.Serializable;

/**
 * Created by MyThinkpad on 2016/11/24.
 */
public class OrderTimeAxis implements Serializable{
    private long time;
    private String desc;

    public OrderTimeAxis(){}

    public OrderTimeAxis(long time, String desc){
        this.time = time;
        this.desc = desc;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
