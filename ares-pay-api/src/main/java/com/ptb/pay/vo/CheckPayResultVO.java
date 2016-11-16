package com.ptb.pay.vo;

import java.io.Serializable;

/**
 * Description:
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-09 16:07  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class CheckPayResultVO implements Serializable{

    private static final long serialVersionUID = 3203507348960523413L;
    private String rechargeOderNo;
    private Long rechargeAmount;
    private boolean payResult;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRechargeOderNo() {
        return rechargeOderNo;
    }

    public void setRechargeOderNo(String rechargeOderNo) {
        this.rechargeOderNo = rechargeOderNo;
    }

    public Long getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Long rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public boolean isPayResult() {
        return payResult;
    }

    public void setPayResult(boolean payResult) {
        this.payResult = payResult;
    }
}
