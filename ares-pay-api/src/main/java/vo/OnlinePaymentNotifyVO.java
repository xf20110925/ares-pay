package vo;

import java.io.Serializable;
import java.util.Map;

/**
 * Description: 充值订单参数VO
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-07 18:35  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class OnlinePaymentNotifyVO implements Serializable{

    private static final long serialVersionUID = 4218745950980971621L;
    private int payType;
    private Map<String, String> params;

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
