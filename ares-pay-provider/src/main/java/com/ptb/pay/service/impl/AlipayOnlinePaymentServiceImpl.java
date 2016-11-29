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
import com.ptb.pay.model.vo.AccountRechargeParamMessageVO;
import com.ptb.pay.service.BusService;
import com.ptb.pay.service.ThirdPaymentNotifyLogService;
import com.ptb.pay.service.interfaces.IOnlinePaymentService;
import com.ptb.pay.utils.alipayweb.util.AlipayNotify;
import com.ptb.pay.utils.alipayweb.util.AlipaySubmit;
import com.ptb.pay.vo.CheckPayResultVO;
import com.ptb.service.api.IBaiduPushApi;
import com.ptb.service.api.ISystemConfigApi;
import com.ptb.utils.encrypt.SignUtil;
import com.ptb.utils.tool.ChangeMoneyUtil;
import enums.MessageTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vo.param.PushMessageParam;

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
    private static final String SYSTEM_CONFIG_ALIPAY_APPID = "alipay.appId";

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

    /**
     * PC支付方式支付宝提供的公钥，用来验签的，跟上面的不一样
     */
    private static final String SYSTEM_CONFIG_ALIPAY_PC_PUBLICKEY = "alipay.pc.publicKey";

    /**
     * PC支付方式同步跳转页面地址
     */
    private static final String SYSTEM_CONFIG_ALIPAY_PC_RETURNURL = "alipay.pc.returnUrl";

    /**
     * PC支付方式异步通知地址
     */
    private static final String SYSTEM_CONFIG_ALIPAY_PC_NOTIFYURL = "alipay.pc.notifyUrl";

    private static AlipayConfig alipayConfig;

    private static Logger LOGGER = LoggerFactory.getLogger(AlipayOnlinePaymentServiceImpl.class);

    @Autowired
    private ISystemConfigApi systemConfigApi;

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Autowired
    private IAccountApi accountApi;

    @Autowired
    private ThirdPaymentNotifyLogService thirdPaymentNotifyLogService;

    @Autowired
    private BusService busService;

    @Autowired
    private IBaiduPushApi baiduPushApi;

    @Override
    public String getPaymentInfo(String rechargeOrderNo, Long price) throws Exception {

        AlipayConfig alipayConfig = getAlipayConfig();
        Map<String, String> toSignParams = new HashMap<String, String>();
        toSignParams.put("app_id", alipayConfig.getAppId());

        Map<String, String> bizContent = new HashMap<String, String>();
        bizContent.put("subject", alipayConfig.getSubject());
        bizContent.put("out_trade_no", rechargeOrderNo);
        bizContent.put("total_amount", ChangeMoneyUtil.fromFenToYuan(String.valueOf(price)));
        bizContent.put("product_code", alipayConfig.getProductCode());
        toSignParams.put("biz_content", JSONObject.toJSONString(bizContent));

        toSignParams.put("notify_url", alipayConfig.getNotifyUrl());
        toSignParams.put("charset", alipayConfig.getCharset());
        toSignParams.put("method", alipayConfig.getMethod());
        toSignParams.put("sign_type", alipayConfig.getSignType());
        toSignParams.put("timestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        toSignParams.put("version", alipayConfig.getVersion());

        String sign = null;
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        String payInfo = null;
        try {
            sign = AlipaySignature.rsaSign(toSignParams, alipayConfig.getPrivateKey(), alipayConfig.getCharset());
            for (String key : toSignParams.keySet()) {
                toSignParams.put(key, URLEncoder.encode(toSignParams.get(key), alipayConfig.getCharset()));
            }
            sign = URLEncoder.encode(sign, alipayConfig.getCharset());
            String orderInfo = AlipaySignature.getSignCheckContentV2(toSignParams);
            payInfo = orderInfo + "&sign=" + sign;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            LOGGER.error("支付宝签名失败", e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LOGGER.error("支付宝签名encode失败", e);
        }


        return payInfo;
    }

    @Override
    public String getPcPaymentInfo(String rechargeOrderNo, Long price) throws Exception {
        AlipayConfig alipayConfig = getAlipayConfig();

        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", alipayConfig.getPcService());
        sParaTemp.put("partner", alipayConfig.getPartner());
        sParaTemp.put("seller_id", alipayConfig.getSellerId());
        sParaTemp.put("_input_charset", alipayConfig.getCharset());
        sParaTemp.put("payment_type", alipayConfig.getPaymentType());
        sParaTemp.put("notify_url", alipayConfig.getPcNotifyUrl());
        sParaTemp.put("return_url", alipayConfig.getPcReturnUrl());
        sParaTemp.put("out_trade_no", rechargeOrderNo);
        sParaTemp.put("qr_pay_mode", "2"); //二维码跳转模式
        sParaTemp.put("subject", alipayConfig.getSubject());
        sParaTemp.put("total_fee", ChangeMoneyUtil.fromFenToYuan(String.valueOf(price)));
        sParaTemp.put("body", alipayConfig.getBody());
        sParaTemp.put("extra_common_param", DeviceTypeEnum.PC.getDeviceType()); //扩展参数

        //其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
        //如sParaTemp.put("参数名","参数值");

        //建立请求
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认", alipayConfig.getPrivateKey(), alipayConfig.getCharset());
        return sHtmlText;
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

        String deviceType = params.get("extra_common_param");
        String rechargeOrderNo = params.get("out_trade_no");
        String rechargeAmount = "";
        if (DeviceTypeEnum.PC.getDeviceType().equalsIgnoreCase(deviceType)) { //pc跟app支付不一样
            rechargeAmount = params.get("total_fee");
        } else {
            rechargeAmount = params.get("total_amount");
        }
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
        if (!String.valueOf(rechargeAmount).equals(ChangeMoneyUtil.fromFenToYuan(String.valueOf(rechargeOrder.getTotalAmount())))) {
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

        if (!DeviceTypeEnum.PC.getDeviceType().equalsIgnoreCase(deviceType)) {
            //验证APPID
            String ourAppId = alipayConfig.getAppId();
            if (!appId.equals(ourAppId)) {
                checkResult = false;
                resultVO.setPayResult(checkResult);
                return resultVO;
            }
        }

        resultVO.setPayResult(checkResult);
        resultVO.setRechargeAmount(rechargeOrder.getTotalAmount());
        resultVO.setRechargeOderNo(rechargeOrderNo);
        return resultVO;
    }

    @Override
    public boolean notifyPayResult(Map<String, String> params) throws Exception {
        thirdPaymentNotifyLogService.asynSaveAlipayNotifyLog(params);
        String rechargeOrderNo = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");
        if ("TRADE_FINISHED".equals(tradeStatus) || "TRADE_SUCCESS".equals(tradeStatus)) {
            AlipayConfig alipayConfig = getAlipayConfig();
            String publickKey = "";
            boolean checkResult = false;
            String deviceType = params.get("extra_common_param");
            if (DeviceTypeEnum.PC.getDeviceType().equalsIgnoreCase(deviceType)) {
                publickKey = alipayConfig.getPcPublickKey();
                checkResult = AlipayNotify.verify(params, alipayConfig.getPartner(), publickKey, alipayConfig.getCharset());
            } else {
                publickKey = alipayConfig.getPublicKey();
                checkResult = AlipaySignature.rsaCheckV1(params, publickKey, alipayConfig.getCharset());
            }
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

                RechargeOrder rechargeOrder = rechargeOrders.get(0);
                AccountRechargeParam rechargeParam = new AccountRechargeParam();
                try {
                    rechargeParam.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(rechargeOrder.getDeviceType()));
                    rechargeParam.setMoney(rechargeOrder.getTotalAmount());
                    rechargeParam.setUserId(rechargeOrder.getUserId());
                    rechargeParam.setPayType(rechargeOrder.getPayType());
                    rechargeParam.setPayMethod(rechargeOrder.getPayMethod());
                    rechargeParam.setPlatformNo(PlatformEnum.xiaomi);
                    rechargeParam.setOrderNo(rechargeOrderNo);
                    //隐式加密
                    TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(rechargeParam), TreeMap.class);
                    String sign = SignUtil.getSignKey(toSign);
                    RpcContext.getContext().setAttachment("key", sign);

                    ResponseVo<PtbAccountVo> repsonseVO = accountApi.recharge(rechargeParam);
                    if (repsonseVO == null || !"0".equals(repsonseVO.getCode())) {
                        sendRetryMessage(rechargeParam);
                    } else {
                        LOGGER.info("充值订单号：" + rechargeOrderNo + "充值成功!");
                        try {
                            //推送消息
                            PushMessageParam param = new PushMessageParam();
                            param.setUserId(rechargeOrder.getUserId());
                            param.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(rechargeOrder.getDeviceType()));
                            param.setTitle("充值成功（在线充值）");
                            param.setMessage("恭喜您，成功充值" + rechargeOrder.getTotalAmount() / 100 + "元，已自动转入钱包余额");
                            param.setMessageType(MessageTypeEnum.ONLINE_RECHARGE.getMessageType());
                            Map<String, Object> keyMap = new HashMap<>();
                            keyMap.put("id", rechargeOrder.getPtbRechargeOrderId());
                            param.setContentParam( keyMap);
                            baiduPushApi.pushMessage(param);
                        }catch (Exception e){
                            LOGGER.error( "线上充值消息推送失败。errorMsg:{}", e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendRetryMessage(rechargeParam);
                    LOGGER.error("支付宝充值失败，放入消息队列重试,params:" + JSONObject.toJSONString(params));
                } finally {
                    rechargeOrder.setStatus(RechargeOrderStatusEnum.paid.getRechargeOrderStatus());
                    rechargeOrder.setPayTime(new Date());
                    rechargeOrderMapper.updateByPrimaryKey(rechargeOrder);
                }
            }
            return checkResult;
        } else {
            return false;
        }
    }

    /**
     * Description: 发送重试消息
     * All Rights Reserved.
     *
     * @param
     * @return
     * @version 1.0  2016-11-16 17:07 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    private void sendRetryMessage(AccountRechargeParam rechargeParam) {
        AccountRechargeParamMessageVO messageVO = new AccountRechargeParamMessageVO();
        messageVO.setAccountRechargeParam(rechargeParam);
        LOGGER.info("发送重试充值消息：" + JSONObject.toJSONString(messageVO));
        busService.sendAccountRechargeRetryMessage(messageVO);
    }

    /**
     * Description: 获取支付宝配置信息
     * All Rights Reserved.
     *
     * @param
     * @return
     * @version 1.0  2016-11-09 23:10 by wgh（guanhua.wang@pintuibao.cn）创建
     */
    public AlipayConfig getAlipayConfig() {
        if (alipayConfig == null) {
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
            params.add(SYSTEM_CONFIG_ALIPAY_PC_PUBLICKEY);
            params.add(SYSTEM_CONFIG_ALIPAY_PC_RETURNURL);
            params.add(SYSTEM_CONFIG_ALIPAY_PC_NOTIFYURL);

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

            alipayConfig.setPcPublickKey(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_PC_PUBLICKEY));
            alipayConfig.setPcReturnUrl(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_PC_RETURNURL));
            alipayConfig.setPcNotifyUrl(alipayInfo.get(SYSTEM_CONFIG_ALIPAY_PC_NOTIFYURL));
        }
        return alipayConfig;
    }

    public static void main(String[] args) {
//        String str= "{\"gmt_create\" : \"2016-11-10 18:24:06\",\"buyer_email\" : \"15010251107\",\"notify_time\" : \"2016-11-10 18:33:21\",\"gmt_payment\" : \"2016-11-10 18:24:08\",\"seller_email\" : \"dayu@pintuibao.cn\",\"quantity\" : \"1\",\"subject\" : \"xiaomirecharge\",\"use_coupon\" : \"N\",\"sign\" : \"MJUoLUcQYhaAPTcET0ck81q5hzXLn08d+iLEpBRiwRUc+w1BXQzNRewLxzBwIs2x8Xq0xLkH/0Fw/euh/ze1UtzeN6BE0+2bG6GCNLBZ4xMiS+mzR0z3I3OmLnUoO2wf4YxYDKN/HbofuQWVwYwJ8QapgN1jS0C8ycWVOdhexVY=\",\"discount\" : \"0.00\",\"body\" : \"xiaomirecharge\",\"buyer_id\" : \"2088302225948663\",\"notify_id\" : \"41161136f0efd054149e9c7ef90404el3e\",\"notify_type\" : \"trade_status_sync\",\"payment_type\" : \"1\",\"out_trade_no\" : \"CZIS1611101823000009\",\"price\" : \"0.01\",\"trade_status\" : \"TRADE_SUCCESS\",\"total_fee\" : \"0.01\",\"trade_no\" : \"2016111021001004660257913006\",\"sign_type\" : \"RSA\",\"seller_id\" : \"2088421215991288\",\"is_total_fee_adjust\" : \"N\"}";
//        Map map = JSONObject.parseObject(str, Map.class);
//        String content = AlipaySignature.getSignCheckContentV2(map);
//        System.out.println(content);
        System.out.println(Integer.valueOf("1.00"));
    }
}
