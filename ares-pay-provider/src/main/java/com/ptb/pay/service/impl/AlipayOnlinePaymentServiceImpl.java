package com.ptb.pay.service.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ptb.account.api.IAccountApi;
import com.ptb.account.vo.PtbAccountVo;
import com.ptb.account.vo.param.AccountRechargeParam;
import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.PlatformEnum;
import com.ptb.common.enums.RechargeOrderStatusEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.conf.payment.AlipayConfig;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.model.RechargeOrderExample;
import com.ptb.pay.service.IOnlinePaymentService;
import com.ptb.service.api.ISystemConfigApi;
import com.ptb.utils.encrypt.SignUtil;
import com.ptb.utils.tool.ChangeMoneyUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vo.CheckPayResultVO;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Description:支付宝相关接口
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:11  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Service
@Transactional
public class AlipayOnlinePaymentServiceImpl implements IOnlinePaymentService {

    /**
     * 支付宝分配的合作方ID
     */
    private static final String SYSTEM_CONFIG_ALIPAY_PARTNER = "alipay.partner";

    /**
     * 支付宝分配的APP_ID
     */
    private static final String SYSTEM_CONFIG_ALIPAY_APPID = "alipay.appid";

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
     * 私钥
     */
    private static final String SYSTEM_CONFIG_ALIPAY_PRIVATEKEY = "alipay.privateKey";

    /**
     * 支付宝提供的公钥，用来验签的，跟上面的私钥不是一对
     */
    private static final String SYSTEM_CONFIG_ALIPAY_PUBLICKEY = "alipay.publicKey";

    private static AlipayConfig alipayConfig;

    private static Logger LOGGER = LoggerFactory.getLogger(AlipayOnlinePaymentServiceImpl.class);

    @Autowired
    private ISystemConfigApi systemConfigApi;

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Autowired
    private IAccountApi accountApi;

    @Override
    public String getPaymentInfo(String rechargeOrderNo, Long price) throws Exception {

        AlipayConfig alipayConfig = getAlipayConfig();
        Map<String, String> toSignParams = new HashMap<String, String>();
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + alipayConfig.getPartner() + "\"";
        toSignParams.put("partner", alipayConfig.getPartner());
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + alipayConfig.getSellerId() + "\"";
        toSignParams.put("seller_id", alipayConfig.getSellerId());
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + rechargeOrderNo + "\"";
        toSignParams.put("out_trade_no", rechargeOrderNo);
        // 商品名称
        orderInfo += "&subject=" + "\"" + alipayConfig.getSubject() + "\"";
        toSignParams.put("subject", alipayConfig.getSubject());
        // 商品详情
        orderInfo += "&body=" + "\"" + alipayConfig.getBody() + "\"";
        toSignParams.put("body", alipayConfig.getBody());

        // 商品金额
        String totalFeeStr = ChangeMoneyUtil.fromFenToYuan(String.valueOf(price));
        orderInfo += "&total_fee=" + "\"" + totalFeeStr + "\"";
        toSignParams.put("total_fee", totalFeeStr);
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + alipayConfig.getNotifyUrl() + "\"";
        toSignParams.put("notify_url", alipayConfig.getNotifyUrl());
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
//        orderInfo += "&return_url=\"" + alipayConfig.getReturnUrl() + "\"";
//        toSignParams.put("return_url", alipayConfig.getReturnUrl());
        String sign = null;
        try {
            sign = AlipaySignature.rsaSign(toSignParams, alipayConfig.getPrivateKey(), alipayConfig.getCharset());
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, alipayConfig.getCharset());
        } catch (AlipayApiException e) {
            e.printStackTrace();
            LOGGER.error("支付宝签名失败", e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LOGGER.error("支付宝签名encode失败", e);
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";
        return payInfo;
    }

    @Override
    public CheckPayResultVO checkPayResult(String payResult) throws Exception {
        CheckPayResultVO resultVO = new CheckPayResultVO();
        if (StringUtils.isBlank(payResult)) {
            resultVO.setPayResult(false);
            return resultVO;
        }
        try {
            JSONObject result = JSONObject.parseObject(payResult);
            String signContent = result.getString("alipay_trade_app_pay_response");
            String sign = result.getString("sign");
            AlipayConfig alipayConfig = getAlipayConfig();
            String publickKey = alipayConfig.getPublicKey();
            boolean checkResult = AlipaySignature.rsaCheckContent(signContent, sign, publickKey, alipayConfig.getCharset());
            if (!checkResult) {
                resultVO.setPayResult(false);
                return resultVO;
            }
            return checkPayResponse(JSONObject.parseObject(signContent, Map.class));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("支付宝签名验证失败：" + payResult);
            resultVO.setPayResult(false);
            return resultVO;
        }
    }

    /**
     * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号；
     * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）；
     * 3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）；
     * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明同步校验结果是无效的，只有全部验证通过后，才可以认定买家付款成功。
     * {"code":"10000","msg":"Success","total_amount":"9.00","app_id":"2014072300007148","trade_no":"2014112400001000340011111118","seller_id":"2088111111116894","out_trade_no":"70501111111S001111119"}
     * Description:
     * All Rights Reserved.
     *
     * @param
     * @return
     * @version 1.0  2016-11-09 11:59 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    public CheckPayResultVO checkPayResponse(Map<String, String> params) throws Exception {
        boolean checkResult = true;
        CheckPayResultVO resultVO = new CheckPayResultVO();

        String rechargeOrderNo = params.get("trade_no");
        String rechargeAmount = params.get("total_amount");
        String sellerId = params.get("seller_id");
        String appId = params.get("app_id");

        RechargeOrderExample example = new RechargeOrderExample();
        example.createCriteria().andRechargeOrderNoEqualTo(rechargeOrderNo);
        List<RechargeOrder> rechargeOrders = rechargeOrderMapper.selectByExample(example);
        //验证订单号
        if (CollectionUtils.isEmpty(rechargeOrders)) {
            checkResult = false;
            resultVO.setPayResult(checkResult);
            return resultVO;
        }
        RechargeOrder rechargeOrder = rechargeOrders.get(0);
        //验证金额
        if (!ChangeMoneyUtil.fromYuanToFen(rechargeAmount).equals(String.valueOf(rechargeOrder.getTotalAmount()))) {
            checkResult = false;
            resultVO.setPayResult(checkResult);
            return resultVO;
        }
        AlipayConfig alipayConfig = getAlipayConfig();
        //验证sellerId
        String ourSellerId = alipayConfig.getSellerId();
        if (!sellerId.equals(ourSellerId)) {
            checkResult = false;
            resultVO.setPayResult(checkResult);
            return resultVO;
        }
        //验证APPID
        String ourAppId = alipayConfig.getAppId();
        if (!appId.equals(ourAppId)) {
            checkResult = false;
            resultVO.setPayResult(checkResult);
            return resultVO;
        }

        resultVO.setPayResult(checkResult);
        resultVO.setRechargeAmount(Long.valueOf(ChangeMoneyUtil.fromYuanToFen(rechargeAmount)));
        resultVO.setRechargeOderNo(rechargeOrderNo);
        return resultVO;
    }

    @Override
    public boolean notifyPayResult(Map<String, String> params) throws Exception {
        String rechargeOrderNo = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");
        if ("TRADE_FINISHED".equals(tradeStatus) || "TRADE_SUCCESS".equals(tradeStatus)) {
            AlipayConfig alipayConfig = getAlipayConfig();
            String publickKey = alipayConfig.getPublicKey();
            boolean checkResult = AlipaySignature.rsaCheckV1(params, publickKey, alipayConfig.getCharset());
            if (checkResult) {
                CheckPayResultVO resultVO = checkPayResponse(params);
                if (!resultVO.isPayResult()) {
                    checkResult = false;
                    return checkResult;
                }

                RechargeOrderExample example = new RechargeOrderExample();
                example.createCriteria().andRechargeOrderNoEqualTo(rechargeOrderNo);
                List<RechargeOrder> rechargeOrders = rechargeOrderMapper.selectByExample(example);
                //验证订单号
                if (CollectionUtils.isEmpty(rechargeOrders)) {
                    checkResult = false;
                    return checkResult;
                }

                try {
                    RechargeOrder rechargeOrder = rechargeOrders.get(0);
                    AccountRechargeParam rechargeParam = new AccountRechargeParam();
                    rechargeParam.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(rechargeOrder.getDeviceType()));
                    rechargeParam.setMoney(rechargeOrder.getTotalAmount());
                    rechargeParam.setUserId(rechargeOrder.getUserId());
                    rechargeParam.setPayType(rechargeOrder.getPayType());
                    rechargeParam.setPayMethod(rechargeOrder.getPayMethod());
                    rechargeParam.setPlatformNo(PlatformEnum.xiaomi);
                    //隐式加密
                    TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(rechargeParam), TreeMap.class);
                    String sign = SignUtil.getSignKey(toSign);
                    RpcContext.getContext().setAttachment("key", sign);

                    ResponseVo<PtbAccountVo> repsonseVO = accountApi.recharge(rechargeParam);
                    if (!"0".equals(repsonseVO.getCode())) {
                        //todo 放入消息队列
                    }

                    rechargeOrder.setStatus(RechargeOrderStatusEnum.paid.getRechargeOrderStatus());
                    rechargeOrder.setPayTime(new Date());
                    rechargeOrderMapper.updateByPrimaryKey(rechargeOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                    //todo 放入消息队列
                    LOGGER.error("支付宝充值失败，放入消息队列重试,params:" + JSONObject.toJSONString(params));
                }
            }
            return checkResult;
        } else {
            return false;
        }
    }

    /**
     * Description: 获取支付宝配置信息
     * All Rights Reserved.
     * @param
     * @return
     * @version 1.0  2016-11-09 23:10 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    public AlipayConfig getAlipayConfig(){
        if(alipayConfig == null){
            alipayConfig = new AlipayConfig();
            List<String> params = new ArrayList<String>();
            params.add(SYSTEM_CONFIG_ALIPAY_APPID);
            params.add(SYSTEM_CONFIG_ALIPAY_PARTNER);
            params.add(SYSTEM_CONFIG_ALIPAY_SUBJECT);
            params.add(SYSTEM_CONFIG_ALIPAY_BODY);
            params.add(SYSTEM_CONFIG_ALIPAY_NOTIFYURL);
            params.add(SYSTEM_CONFIG_ALIPAY_RETURNURL);
            params.add(SYSTEM_CONFIG_ALIPAY_PRIVATEKEY);
            params.add(SYSTEM_CONFIG_ALIPAY_PUBLICKEY);
            ResponseVo<Map<String, String>> result = systemConfigApi.getConfigs(params);
            if (result == null || CollectionUtils.isEmpty(result.getData())) {
                return null;
            }
            Map<String, String> alipayInfo = result.getData();
            alipayConfig.setAppId(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_APPID));
            alipayConfig.setPartner(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_PARTNER));
            alipayConfig.setSellerId(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_PARTNER));
            alipayConfig.setSubject(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_SUBJECT));
            alipayConfig.setBody(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_BODY));
            alipayConfig.setNotifyUrl(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_NOTIFYURL));
            alipayConfig.setReturnUrl(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_RETURNURL));
            alipayConfig.setPrivateKey(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_PRIVATEKEY));
            alipayConfig.setPublicKey(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_PUBLICKEY));
        }
        return alipayConfig;
    }

    public static void main(String[] args) {
        String str= "a=123";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMmkRvUqD8SFv65qQB+RbnT+Bt5GGS1oEqNR3phTkx6f0+mMcRysvTNtuFxgU/iCL3WLiV9GFQVcS4GGM+XyyRK4cxQAzA2Nxz+a0H4zeuJgZsCyCs0Izwg+nyjz5RYAS8zEVqGAGjA1mxGYkrsHzs5SLf9PK0VbVqC9CoY1QKJLAgMBAAECgYAZPeNUFWvb4hJYtxxH12DIbHRXptbIBFsw7rK6xtuH3eIdPmN6f3U/1uBZUAWy+jkOseUEdDV1clRRZodgpb/dywx/rcmvCT0XR5M88GpLPfz+mGVUIpc3l7kLWCcRb+GB8Y5oGqesX+twMJNMwmD9adN1SDWyGWVpuo7FBGLDoQJBAPNoHWCz4pCcXvJMUQOMRHvJXKE/8BQjEUFmyH/up/8V6y+wi4I8uQVvWbHfYrxJSs73bFFa+08RS8J0Ab7i/+8CQQDUEv3LHSKI9XCNOkTJ2wf6k0Mx/wUmPo6M3WoGzBEsbh3lhPUhsc9SYGNGU5GIoJsD31rVh5LYPL8y/WFKT+dlAkBPju9TRVi/pazPC6zLORTFVSrGtexV2KRyORM95ZbZpKNrwgvHdOuQ4DV0EyzlmBswjOTFrrRHwsJ9e4d6ih3XAkAVCXE9hb1YQqiQidgrAdwvwg3nVrnHCmuPk6Mh23pUovO6Qc8jMYU0UbVnU4DbQbs+zhgvmEw7wQLiZvoQqg51AkBcct0bJoq56xuY3vIG12jdVlV2Q2RrnGXGrHY6tJQWvEFYk2ZN4sGIU2onTJxuhrpywk8rlrLtrprrrxKkYbPL";
        try {
            System.out.println(AlipaySignature.rsaSign(str,privateKey ,"GBK"));
//            String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJpEb1Kg/Ehb+uakAfkW50/gbeRhktaBKjUd6YU5Men9PpjHEcrL0zbbhcYFP4gi91i4lfRhUFXEuBhjPl8skSuHMUAMwNjcc/mtB+M3riYGbAsgrNCM8IPp8o8+UWAEvMxFahgBowNZsRmJK7B87OUi3/TytFW1agvQqGNUCiSwIDAQAB";
//            System.out.println(AlipaySignature.rsaDecrypt("EzI3QgZI2E24uEu9dInkLn4pBy+u5ejRVjyR2qlVRNwIykXJA0FM2JU3DP3r1XQRaZLk+BbIdrPGma5jyuw2CRM/aOo4U7vCzDn+5xXUt8sI8NHI1NxcfWend91p0Kw89Tk+z4rvuvbgrc9MwyXXOFRzD1H3YDOs6ckcfPbpl24=",publicKey,"utf-8"));
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }
}
