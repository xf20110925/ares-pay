package com.ptb.pay.service.impl;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.conf.payment.AlipayConfig;
import com.ptb.pay.service.interfaces.IOnlinePaymentService;
import com.ptb.pay.utils.wxpay.GetWxOrderno;
import com.ptb.pay.utils.wxpay.RequestHandler;
import com.ptb.pay.vo.CheckPayResultVO;
import com.ptb.service.api.ISystemConfigApi;
import com.ptb.utils.date.DateUtil;
import com.ptb.utils.tool.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Description: 微信支付相关接口
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:12  by wgh（guanhua.wang@pintuibao.cn）创建
 */
public class WxpayOnlinePaymentServiceImpl implements IOnlinePaymentService{

    private static final String SYSTEM_CONFIG_WXPAY_APPID = "wxpay.appid";
    private static final String SYSTEM_CONFIG_WXPAY_MCH_ID = "wxpay.mch_id";
    private static final String SYSTEM_CONFIG_WXPAY_NOTIFY_URL = "wxpay.notify.url";
    private static final String SYSTEM_CONFIG_WXPAY_API_KEY = "wxpay.api.key";
    private static final String SYSTEM_CONFIG_WXPAY_CREATE_ORDER_URL = "wxpay.create.order.url";

    @Autowired
    private ISystemConfigApi systemConfigApi;

    @Override
    public String getPaymentInfo(String rechargeOrderNo, Long price) throws Exception{
        Map<String, String> config = getWXPayConfig();
        String appid = config.get("SYSTEM_CONFIG_WXPAY_APPID");
        String mchId = config.get("SYSTEM_CONFIG_WXPAY_MCH_ID");
        String notifyUrl = config.get("SYSTEM_CONFIG_WXPAY_NOTIFY_URL");
        String apiKey = config.get("SYSTEM_CONFIG_WXPAY_API_KEY");
        String createOrderURL = config.get("SYSTEM_CONFIG_WXPAY_CREATE_ORDER_URL");
        String currTime = DateUtil.getCurrTime();
        //8位日期
        String strTime = currTime.substring(8, currTime.length());
        //四位随机数
        String strRandom = RandomUtil.buildRandom(4) + "";
        //10位序列号,可以自行调整。
        String nonceStr = strTime + strRandom;
        String body = "品推宝小蜜-充值";
        String tradeType = "APP";
        //IP地址
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("body", body);
        packageParams.put("mch_id", mchId);
        packageParams.put("nonce_str", nonceStr);
        packageParams.put("notify_url", notifyUrl);
        packageParams.put("out_trade_no", rechargeOrderNo);
//        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("total_fee", String.valueOf(price));
        packageParams.put("trade_type", tradeType);
        RequestHandler reqHandler = new RequestHandler(apiKey);
        String sign = reqHandler.createSign(packageParams);//获取签名
        String xml="<xml>"+
                "<appid>"+appid+"</appid>"+
                "<body><![CDATA["+body+"]]></body>"+
                "<mch_id>"+mchId+"</mch_id>"+
                "<nonce_str>"+nonceStr+"</nonce_str>"+
                "<notify_url>"+notifyUrl+"</notify_url>"+
                "<out_trade_no>"+rechargeOrderNo+"</out_trade_no>"+
//                "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
                "<total_fee>"+String.valueOf(price)+"</total_fee>"+
                "<trade_type>"+tradeType+"</trade_type>"+
                "<sign>"+sign+"</sign>"+
                "</xml>";
        String prepay_id="";
        try {
            prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);
            if(prepay_id.equals("")){
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getPcPaymentInfo(String rechargeOrderNo, Long price) throws Exception {
        return null;
    }

    @Override
    public CheckPayResultVO checkPayResult(String payResult) throws Exception {
        return null;
    }

    @Override
    public boolean notifyPayResult(Map<String, String> params) throws Exception {
        return false;
    }

    public Map<String, String> getWXPayConfig() {
        List<String> params = new ArrayList<String>();
        params.add(SYSTEM_CONFIG_WXPAY_APPID);
        params.add(SYSTEM_CONFIG_WXPAY_MCH_ID);
        params.add(SYSTEM_CONFIG_WXPAY_NOTIFY_URL);
        params.add(SYSTEM_CONFIG_WXPAY_API_KEY);
        params.add(SYSTEM_CONFIG_WXPAY_CREATE_ORDER_URL);

        ResponseVo<Map<String, String>> result = systemConfigApi.getConfigs(params);
        if ( !"0".equals( result.getCode())) {
            return new HashMap<>();
        }
        return result.getData();
    }
}
