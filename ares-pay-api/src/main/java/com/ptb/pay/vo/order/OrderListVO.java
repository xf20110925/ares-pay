package com.ptb.pay.vo.order;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyThinkpad on 2016/11/21.
 */
public class OrderListVO implements Serializable{
    private int totalNum;
    private List<OrderVO> orderVOList;

    public OrderListVO(){}

    public OrderListVO(int num, List<OrderVO> orderVOs){
        this.totalNum = num;
        this.orderVOList = orderVOs;
    }

    public static OrderListVO Empty(){
        return new OrderListVO(0, new ArrayList<>());
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<OrderVO> getOrderVOList() {
        return orderVOList;
    }

    public void setOrderVOList(List<OrderVO> orderVOList) {
        this.orderVOList = orderVOList;
    }
}
