package com.ptb.pay.service.impl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ptb.account.api.IAccountApi;
import com.ptb.account.vo.PtbAccountVo;
import com.ptb.account.vo.param.AccountRechargeParam;
import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.PlatformEnum;
import com.ptb.common.enums.RechargeOrderStatusEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.model.RechargeOrderExample;
import com.ptb.pay.model.vo.AccountRechargeParamMessageVO;
import com.ptb.pay.service.BusService;
import com.ptb.pay.service.ThirdPaymentNotifyLogService;
import com.ptb.pay.service.interfaces.IOnlinePaymentService;
import com.ptb.pay.utils.wxpay.GetWxOrderno;
import com.ptb.pay.utils.wxpay.RequestHandler;
import com.ptb.pay.vo.CheckPayResultVO;
import com.ptb.service.api.IBaiduPushApi;
import com.ptb.service.api.ISystemConfigApi;
import com.ptb.utils.date.DateUtil;
import com.ptb.utils.encrypt.SignUtil;
import com.ptb.utils.tool.ChangeMoneyUtil;
import com.ptb.utils.tool.RandomUtil;
import enums.MessageTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vo.param.PushMessageParam;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Description: 微信支付相关接口
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-08 13:12  by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Service
public class WxpayOnlinePaymentServiceImpl implements IOnlinePaymentService{

    private static Logger LOGGER = LoggerFactory.getLogger(WxpayOnlinePaymentServiceImpl.class);

    private static final String SYSTEM_CONFIG_WXPAY_APPID = "wxpay.appid";
    private static final String SYSTEM_CONFIG_WXPAY_MCH_ID = "wxpay.mch.id";
    private static final String SYSTEM_CONFIG_WXPAY_NOTIFY_URL = "wxpay.notify.url";
    private static final String SYSTEM_CONFIG_WXPAY_API_KEY = "wxpay.api.key";
    private static final String SYSTEM_CONFIG_WXPAY_CREATE_ORDER_URL = "wxpay.create.order.url";

    @Autowired
    private ISystemConfigApi systemConfigApi;
    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;
    @Autowired
    private IAccountApi accountApi;
    @Autowired
    private IBaiduPushApi baiduPushApi;
    @Autowired
    private BusService busService;
    @Autowired
    private ThirdPaymentNotifyLogService thirdPaymentNotifyLogService;

    @Override
    public String getPaymentInfo(String rechargeOrderNo, Long price) throws Exception{
        Map<String, String> config = getWXPayConfig();
        String appid = config.get(SYSTEM_CONFIG_WXPAY_APPID);
        String mchId = config.get(SYSTEM_CONFIG_WXPAY_MCH_ID);
        String notifyUrl = config.get(SYSTEM_CONFIG_WXPAY_NOTIFY_URL);
        String apiKey = config.get(SYSTEM_CONFIG_WXPAY_API_KEY);
        String createOrderURL = config.get(SYSTEM_CONFIG_WXPAY_CREATE_ORDER_URL);
        String partnerid = mchId;
        String currTime = DateUtil.getCurrTime();
        //8位日期
        String strTime = currTime.substring(8, currTime.length());
        //四位随机数
        String strRandom = RandomUtil.buildRandom(4) + "";
        //10位序列号,可以自行调整。
        String nonceStr = strTime + strRandom;
        String body = "品推宝小蜜-充值";
        String tradeType = "APP";
        String limitPay = "no_credit";
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("body", body);
        packageParams.put("mch_id", mchId);
        packageParams.put("nonce_str", nonceStr);
        packageParams.put("notify_url", notifyUrl);
        packageParams.put("out_trade_no", rechargeOrderNo);
        packageParams.put("total_fee", String.valueOf(price));
        packageParams.put("trade_type", tradeType);
        packageParams.put("limit_pay", limitPay);
        RequestHandler reqHandler = new RequestHandler(apiKey);
        String sign = reqHandler.createSign(packageParams);//获取签名
        String xml="<xml>"+
                "<appid>"+appid+"</appid>"+
                "<body><![CDATA["+body+"]]></body>"+
                "<mch_id>"+mchId+"</mch_id>"+
                "<nonce_str>"+nonceStr+"</nonce_str>"+
                "<notify_url>"+notifyUrl+"</notify_url>"+
                "<out_trade_no>"+rechargeOrderNo+"</out_trade_no>"+
                "<total_fee>"+String.valueOf(price)+"</total_fee>"+
                "<trade_type>"+tradeType+"</trade_type>"+
                "<limit_pay>"+limitPay+"</limit_pay>"+
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

        //获取到prepayid后对以下字段进行签名最终发送给app
        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
        String timestamp = DateUtil.getTimeStamp();
        finalpackage.put("appid", appid);
        finalpackage.put("timestamp", timestamp);
        finalpackage.put("noncestr", nonceStr);
        finalpackage.put("partnerid", partnerid);
        finalpackage.put("package", "Sign=WXPay");
        finalpackage.put("prepayid", prepay_id);
        String finalsign = reqHandler.createSign(finalpackage);
        return reqHandler.createParamStr( finalpackage).append("sign=").append( finalsign).toString();
    }

    @Override
    public String getPcPaymentInfo(String rechargeOrderNo, Long price) throws Exception {
        Map<String, String> config = getWXPayConfig();
        String appid = config.get(SYSTEM_CONFIG_WXPAY_APPID);
        String mchId = config.get(SYSTEM_CONFIG_WXPAY_MCH_ID);
        String notifyUrl = config.get(SYSTEM_CONFIG_WXPAY_NOTIFY_URL);
        String apiKey = config.get(SYSTEM_CONFIG_WXPAY_API_KEY);
        String createOrderURL = config.get(SYSTEM_CONFIG_WXPAY_CREATE_ORDER_URL);
        String partnerid = mchId;
        String currTime = DateUtil.getCurrTime();
        //8位日期
        String strTime = currTime.substring(8, currTime.length());
        //四位随机数
        String strRandom = RandomUtil.buildRandom(4) + "";
        //10位序列号,可以自行调整。
        String nonceStr = strTime + strRandom;
        String body = "品推宝小蜜-充值";
        String tradeType = "NATIVE";
        String limitPay = "no_credit";
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("body", body);
        packageParams.put("mch_id", mchId);
        packageParams.put("nonce_str", nonceStr);
        packageParams.put("notify_url", notifyUrl);
        packageParams.put("out_trade_no", rechargeOrderNo);
        packageParams.put("total_fee", String.valueOf(price));
        packageParams.put("trade_type", tradeType);
        packageParams.put("limit_pay", limitPay);
        RequestHandler reqHandler = new RequestHandler(apiKey);
        String sign = reqHandler.createSign(packageParams);//获取签名
        String xml="<xml>"+
                "<appid>"+appid+"</appid>"+
                "<body><![CDATA["+body+"]]></body>"+
                "<mch_id>"+mchId+"</mch_id>"+
                "<nonce_str>"+nonceStr+"</nonce_str>"+
                "<notify_url>"+notifyUrl+"</notify_url>"+
                "<out_trade_no>"+rechargeOrderNo+"</out_trade_no>"+
                "<total_fee>"+String.valueOf(price)+"</total_fee>"+
                "<trade_type>"+tradeType+"</trade_type>"+
                "<limit_pay>"+limitPay+"</limit_pay>"+
                "<sign>"+sign+"</sign>"+
                "</xml>";
        String code_url="";
        try {
            code_url = new GetWxOrderno().getCodeUrl(createOrderURL, xml);
            if(code_url.equals("")){
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code_url;
    }

    @Override
    public CheckPayResultVO checkPayResult(String payResult) throws Exception {
//        LOGGER.info( payResult);
//        CheckPayResultVO resultVO = new CheckPayResultVO();
//        if (StringUtils.isBlank(payResult)) {
//            resultVO.setPayResult(false);
//            return resultVO;
//        }
//        String rechargeOrderNo = "test";
//        RechargeOrderExample example = new RechargeOrderExample();
//        example.createCriteria().andRechargeOrderNoEqualTo(rechargeOrderNo);
//        List<RechargeOrder> rechargeOrders = rechargeOrderMapper.selectByExample(example);
//        if ( CollectionUtils.isEmpty( rechargeOrders)){
//            resultVO.setPayResult(false);
//            return resultVO;
//        }
//        RechargeOrder order = rechargeOrders.get( 0);
//        resultVO.setPayResult( true);
//        resultVO.setRechargeAmount( order.getTotalAmount());
//        resultVO.setRechargeOderNo( order.getRechargeOrderNo());
//        return resultVO;
        return null;
    }

    @Override
    public boolean notifyPayResult(Map<String, String> params) throws Exception {
        thirdPaymentNotifyLogService.asynSaveWxpayNotifyLog( params);
        String msgxml = params.get("xml");
        Map map =  new GetWxOrderno().doXMLParse(msgxml);
        String result_code=(String) map.get("result_code");
        String out_trade_no  = (String) map.get("out_trade_no");
        String sign  = (String) map.get("sign");
        String sn=out_trade_no.split("\\|")[0];//获取订单编号
        RechargeOrderExample example = new RechargeOrderExample();
        example.createCriteria().andRechargeOrderNoEqualTo(sn);
        List<RechargeOrder> rechargeOrders = rechargeOrderMapper.selectByExample(example);
        if(result_code.equals("SUCCESS") && !CollectionUtils.isEmpty( rechargeOrders)){
            RechargeOrder rechargeOrder = rechargeOrders.get( 0);
            //总金额以分为单位，不带小数点
            String order_total_fee = String.valueOf(rechargeOrder.getTotalAmount());
            String fee_type  = (String) map.get("fee_type");
            String bank_type  = (String) map.get("bank_type");
            String cash_fee  = (String) map.get("cash_fee");
            String is_subscribe  = (String) map.get("is_subscribe");
            String nonce_str  = (String) map.get("nonce_str");
            String openid  = (String) map.get("openid");
            String return_code  = (String) map.get("return_code");
            String sub_mch_id  = (String) map.get("sub_mch_id");
            String time_end  = (String) map.get("time_end");
            String trade_type  = (String) map.get("trade_type");
            String transaction_id  = (String) map.get("transaction_id");
            Map<String, String> config = getWXPayConfig();
            String appid = config.get(SYSTEM_CONFIG_WXPAY_APPID);
            String mchId = config.get(SYSTEM_CONFIG_WXPAY_MCH_ID);
            String apiKey = config.get(SYSTEM_CONFIG_WXPAY_API_KEY);
            //需要对以下字段进行签名
            SortedMap<String, String> packageParams = new TreeMap<String, String>();
            packageParams.put("appid", appid);
            packageParams.put("bank_type", bank_type);
            packageParams.put("cash_fee", cash_fee);
            packageParams.put("fee_type", fee_type);
            packageParams.put("is_subscribe", is_subscribe);
            packageParams.put("mch_id", mchId);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("openid", openid);
            packageParams.put("out_trade_no", out_trade_no);
            packageParams.put("result_code", result_code);
            packageParams.put("return_code", return_code);
            packageParams.put("sub_mch_id", sub_mch_id);
            packageParams.put("time_end", time_end);
            packageParams.put("total_fee", order_total_fee);    //用自己系统的数据：订单金额
            packageParams.put("trade_type", trade_type);
            packageParams.put("transaction_id", transaction_id);
            RequestHandler reqHandler = new RequestHandler(apiKey);
            String endsign = reqHandler.createSign(packageParams);
            if(!sign.equals(endsign)){//验证签名是否正确  官方签名工具地址：https://pay.weixin.qq.com/wiki/tools/signverify/
                LOGGER.error( "微信充值，验签失败。sign:{} params:{}", sign, JSON.toJSONString( packageParams));
                return false;
            }
            AccountRechargeParam rechargeParam = new AccountRechargeParam();
            try {
                rechargeParam.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(rechargeOrder.getDeviceType()));
                rechargeParam.setMoney(rechargeOrder.getTotalAmount());
                rechargeParam.setUserId(rechargeOrder.getUserId());
                rechargeParam.setPayType(rechargeOrder.getPayType());
                rechargeParam.setPayMethod(rechargeOrder.getPayMethod());
                rechargeParam.setPlatformNo(PlatformEnum.xiaomi);
                rechargeParam.setOrderNo(sn);
                //隐式加密
                TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(rechargeParam), TreeMap.class);
                String signKey = SignUtil.getSignKey(toSign);
                RpcContext.getContext().setAttachment("key", signKey);

                ResponseVo<PtbAccountVo> repsonseVO = accountApi.recharge(rechargeParam);
                if (repsonseVO == null || !"0".equals(repsonseVO.getCode())) {
                    sendRetryMessage(rechargeParam);
                } else {
                    LOGGER.info("充值订单号：{} 充值成功!", sn);
                    try {
                        //推送消息
                        PushMessageParam param = new PushMessageParam();
                        param.setUserId(rechargeOrder.getUserId());
                        param.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(rechargeOrder.getDeviceType()));
                        param.setTitle("充值成功（在线充值）");
                        param.setMessage("恭喜您，成功充值" + ChangeMoneyUtil.fromFenToYuan(rechargeOrder.getTotalAmount()) + "元，已自动转入钱包余额");
                        param.setMessageType(MessageTypeEnum.ONLINE_RECHARGE.getMessageType());
                        Map<String, Object> keyMap = new HashMap<>();
                        keyMap.put("id", rechargeOrder.getPtbRechargeOrderId());
                        param.setContentParam( keyMap);
                        param.setNeedSaveMessage( true);
                        param.setNeedPushMessage( true);
                        baiduPushApi.pushMessage(param);
                    }catch (Exception e){
                        LOGGER.error( "线上充值消息推送失败。errorMsg:{}", e.getMessage());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendRetryMessage(rechargeParam);
                LOGGER.error("微信充值失败，放入消息队列重试,params:" + JSONObject.toJSONString(params));
            } finally {
                rechargeOrder.setStatus(RechargeOrderStatusEnum.paid.getRechargeOrderStatus());
                rechargeOrder.setPayTime(new Date());
                rechargeOrderMapper.updateByPrimaryKey(rechargeOrder);
            }
        }
        return true;
    }

    private void sendRetryMessage(AccountRechargeParam rechargeParam) {
        AccountRechargeParamMessageVO messageVO = new AccountRechargeParamMessageVO();
        messageVO.setAccountRechargeParam(rechargeParam);
        LOGGER.info("发送重试充值消息：" + JSONObject.toJSONString(messageVO));
        busService.sendAccountRechargeRetryMessage(messageVO);
    }

    private LoadingCache<String, Map<String, String>> wxPayConfigCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES) //缓存10分钟
            .build(new CacheLoader<String, Map<String, String>>() {
                @Override
                public Map<String, String> load(String s) {
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
            });

    public Map<String, String> getWXPayConfig() throws ExecutionException {
        return wxPayConfigCache.get("ptb.pay.wxpay.config");

    }
}
