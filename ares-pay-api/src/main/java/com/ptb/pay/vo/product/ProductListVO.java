package com.ptb.pay.vo.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyThinkpad on 2016/11/18.
 */
public class ProductListVO implements Serializable {

    private Integer totalNum;

    private List<ProductVO> list;

    public ProductListVO(){}

    public ProductListVO(int totalNum, List<ProductVO> list){
        this.totalNum = totalNum;
        this.list = list;
    }

    public static ProductListVO Empty(){
        return new ProductListVO(0,new ArrayList<>());
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public List<ProductVO> getList() {
        return list;
    }

    public void setList(List<ProductVO> list) {
        this.list = list;
    }
}
