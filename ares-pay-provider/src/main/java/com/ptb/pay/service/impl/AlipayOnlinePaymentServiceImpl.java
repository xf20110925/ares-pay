package com.ptb.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.service.IOnlinePaymentService;
import com.ptb.service.api.ISystemConfigApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Description:支付宝相关接口
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:11  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Service
@Transactional
public class AlipayOnlinePaymentServiceImpl implements IOnlinePaymentService{

    /**
     * 支付宝分配的合作方ID
     */
    private static final String SYSTEM_CONFIG_ALIPAY_PARTNER = "alipay.partner";

    /**
     * 支付时显示的商品标题
     */
    private static final String SYSTEM_CONFIG_ALIPAY_SUBJECT = "alipay.subject";

    /**
     * 支付时显示的商品描述
     */
    private static final String SYSTEM_CONFIG_ALIPAY_BODY = "alipay.body";

    /**
     * 异步通知结果
     */
    private static final String SYSTEM_CONFIG_ALIPAY_NOTIFYURL = "alipay.notifyurl";

    /**
     * 同步通知地址
     */
    private static final String SYSTEM_CONFIG_ALIPAY_RETURNURL = "alipay.returnurl";

    /**
     *  私钥
     */
    private static final String SYSTEM_CONFIG_ALIPAY_PRIVATEKEY = "alipay.privateKey";

    /**
     *  签名字符集
     */
    private static final String ALIPAY_SIGN_CHARSET = "UTF-8";

    @Autowired
    private ISystemConfigApi systemConfigApi;

    @Override
    public String getPaymentInfo(String rechargeOrderNo, Long price) {

        List<String> params = new ArrayList<String>();
        params.add(SYSTEM_CONFIG_ALIPAY_PARTNER);
        params.add(SYSTEM_CONFIG_ALIPAY_SUBJECT);
        params.add(SYSTEM_CONFIG_ALIPAY_BODY);
        params.add(SYSTEM_CONFIG_ALIPAY_NOTIFYURL);
        params.add(SYSTEM_CONFIG_ALIPAY_RETURNURL);
        params.add(SYSTEM_CONFIG_ALIPAY_PRIVATEKEY);
        ResponseVo<Map<String, String>> result = systemConfigApi.getConfigs(params);
        if (result == null || CollectionUtils.isEmpty(result.getData())) {
            return null;
        }
        Map<String, String> alipayInfo = result.getData();

        Map<String, String> toSignParams = Collections.emptyMap();
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + alipayInfo.get(SYSTEM_CONFIG_ALIPAY_PARTNER) + "\"";
        toSignParams.put("partner", alipayInfo.get(SYSTEM_CONFIG_ALIPAY_PARTNER));
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + alipayInfo.get(SYSTEM_CONFIG_ALIPAY_PARTNER) + "\"";
        toSignParams.put("seller_id", alipayInfo.get(SYSTEM_CONFIG_ALIPAY_PARTNER));
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + rechargeOrderNo + "\"";
        toSignParams.put("out_trade_no", rechargeOrderNo);
        // 商品名称
        orderInfo += "&subject=" + "\"" + alipayInfo.get(SYSTEM_CONFIG_ALIPAY_SUBJECT) + "\"";
        toSignParams.put("subject", alipayInfo.get(SYSTEM_CONFIG_ALIPAY_SUBJECT));
        // 商品详情
        orderInfo += "&body=" + "\"" + alipayInfo.get(SYSTEM_CONFIG_ALIPAY_BODY) + "\"";
        toSignParams.put("body", alipayInfo.get(SYSTEM_CONFIG_ALIPAY_BODY));
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        toSignParams.put("total_fee", String.valueOf(price));
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + alipayInfo.get(SYSTEM_CONFIG_ALIPAY_NOTIFYURL) + "\"";
        toSignParams.put("notify_url", alipayInfo.get(SYSTEM_CONFIG_ALIPAY_NOTIFYURL));
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        toSignParams.put("service", "mobile.securitypay.pay");
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        toSignParams.put("payment_type", "1");
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        toSignParams.put("_input_charset", "utf-8");
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"" + alipayInfo.get(SYSTEM_CONFIG_ALIPAY_RETURNURL) + "\"";
        toSignParams.put("return_url", alipayInfo.get(SYSTEM_CONFIG_ALIPAY_RETURNURL));
        String sign = null;
        try {
            sign = AlipaySignature.rsaSign(toSignParams, alipayInfo.get(SYSTEM_CONFIG_ALIPAY_RETURNURL), ALIPAY_SIGN_CHARSET);
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, ALIPAY_SIGN_CHARSET);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";
        return payInfo;
    }

}
