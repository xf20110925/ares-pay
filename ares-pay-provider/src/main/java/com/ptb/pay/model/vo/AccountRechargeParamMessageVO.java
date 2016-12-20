package com.ptb.pay.model.vo;

import com.ptb.account.vo.param.AccountRechargeParam;

/**
 * Description:
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-16 16:48  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class AccountRechargeParamMessageVO {

    private AccountRechargeParam accountRechargeParam;
    private Long adminId;
    private String remarks;
    private int sendTimes = 0;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public AccountRechargeParam getAccountRechargeParam() {
        return accountRechargeParam;
    }

    public void setAccountRechargeParam(AccountRechargeParam accountRechargeParam) {
        this.accountRechargeParam = accountRechargeParam;
    }

    public int getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(int sendTimes) {
        this.sendTimes = sendTimes;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
