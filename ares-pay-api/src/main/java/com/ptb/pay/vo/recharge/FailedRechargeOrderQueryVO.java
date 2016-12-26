package com.ptb.pay.vo.recharge;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 失败充值订单查询VO
 * All Rights Reserved.
 *
 * @version 1.0  2016-12-01 18:49  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class FailedRechargeOrderQueryVO implements Serializable {

    private static final long serialVersionUID = 8737226171274311455L;
    private String rechargeOrderNo;
    private Integer status;
    private Date startTime;
    private Date endTime;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRechargeOrderNo() {
        return rechargeOrderNo;
    }

    public void setRechargeOrderNo(String rechargeOrderNo) {
        this.rechargeOrderNo = rechargeOrderNo;
    }
}
