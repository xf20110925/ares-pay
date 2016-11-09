package vo;

import java.io.Serializable;

/**
 * Description: 充值订单参数VO
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-07 18:35  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class RechargeOrderParamsVO implements Serializable{

    private static final long serialVersionUID = -6620090769182151799L;
    private Long rechargeAmount;
    private int payMethod;
    private int payType;
    private String orderNo;
    private String deviceType;
    private Long userId;
    private String payResult;

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Long rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }
}
