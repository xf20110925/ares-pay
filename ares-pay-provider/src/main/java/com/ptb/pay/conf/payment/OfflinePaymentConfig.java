package com.ptb.pay.conf.payment;

/**
 * Description: 线下充值方式配置
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-09 22:53  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class OfflinePaymentConfig {

    private String bankName;
    private String openAccountBankName;
    private String openAccountUserName;
    private String openAccountUserNum;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOpenAccountBankName() {
        return openAccountBankName;
    }

    public void setOpenAccountBankName(String openAccountBankName) {
        this.openAccountBankName = openAccountBankName;
    }

    public String getOpenAccountUserName() {
        return openAccountUserName;
    }

    public void setOpenAccountUserName(String openAccountUserName) {
        this.openAccountUserName = openAccountUserName;
    }

    public String getOpenAccountUserNum() {
        return openAccountUserNum;
    }

    public void setOpenAccountUserNum(String openAccountUserNum) {
        this.openAccountUserNum = openAccountUserNum;
    }
}
