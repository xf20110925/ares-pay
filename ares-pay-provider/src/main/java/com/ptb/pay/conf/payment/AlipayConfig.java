package com.ptb.pay.conf.payment;

/**
 * Description:
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-09 22:53  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class AlipayConfig {

    private String appId;
    private String sellerId;
    private String partner;
    private String subject;
    private String body;
    private String notifyUrl;
    private String returnUrl;
    private String privateKey;
    private String publicKey;
    private String charset = "UTF-8";
    private String version = "1.0";
    private String method = "alipay.trade.app.pay";
    private String signType = "RSA";
    private String productCode = "QUICK_MSECURITY_PAY";
    private String enablePayChannels = "balance,moneyFund,debitCardExpress"; //可用支付渠道，余额，余额宝，借记卡快捷
//    -------------------pc start------------------------------------
    private String pcNotifyUrl;
    private String pcReturnUrl;
    private String pcPublickKey;
    // 支付类型 ，无需修改
    private String paymentType = "1";
    // 调用的接口名，无需修改
    private String pcService = "create_direct_pay_by_user";
    private String enablePaymethod = "directPay^bankPay^debitCardExpress"; //pc 可用支付渠道，余额，网银，借记卡快捷

    public String getEnablePaymethod() {
        return enablePaymethod;
    }

    public void setEnablePaymethod(String enablePaymethod) {
        this.enablePaymethod = enablePaymethod;
    }

    public String getEnablePayChannels() {
        return enablePayChannels;
    }

    public void setEnablePayChannels(String enablePayChannels) {
        this.enablePayChannels = enablePayChannels;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getPcNotifyUrl() {
        return pcNotifyUrl;
    }

    public void setPcNotifyUrl(String pcNotifyUrl) {
        this.pcNotifyUrl = pcNotifyUrl;
    }

    public String getPcReturnUrl() {
        return pcReturnUrl;
    }

    public void setPcReturnUrl(String pcReturnUrl) {
        this.pcReturnUrl = pcReturnUrl;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPcService() {
        return pcService;
    }

    public void setPcService(String pcService) {
        this.pcService = pcService;
    }

    public String getPcPublickKey() {
        return pcPublickKey;
    }

    public void setPcPublickKey(String pcPublickKey) {
        this.pcPublickKey = pcPublickKey;
    }
}
