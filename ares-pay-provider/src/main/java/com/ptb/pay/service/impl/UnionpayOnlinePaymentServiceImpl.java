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
import com.ptb.pay.service.interfaces.IOnlinePaymentService;
import com.ptb.pay.utils.unionpay.AcpService;
import com.ptb.pay.utils.unionpay.LogUtil;
import com.ptb.pay.utils.unionpay.SDKConfig;
import com.ptb.pay.vo.CheckPayResultVO;
import com.ptb.service.api.IBaiduPushApi;
import com.ptb.service.api.ISystemConfigApi;
import com.ptb.utils.date.DateUtil;
import com.ptb.utils.encrypt.SignUtil;
import com.ptb.utils.tool.ChangeMoneyUtil;
import enums.MessageTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vo.param.PushMessageParam;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by zuokui.fu on 2016/12/8.
 */
@Service
public class UnionpayOnlinePaymentServiceImpl implements IOnlinePaymentService {

    private static Logger LOGGER = LoggerFactory.getLogger(UnionpayOnlinePaymentServiceImpl.class);

    private static final String SYSTEM_CONFIG_UNIONPAY_MER_ID = "unionpay.mer.id";
    private static final String SYSTEM_CONFIG_UNIONPAY_NOTIFY_URL = "unionpay.notify.url";

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

    @PostConstruct
    private void init(){
        SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
    }

    @Override
    public String getPaymentInfo(String rechargeOrderNo, Long price) throws Exception {
        Map<String, String> payConfig = getUnionPayConfig();
        String merId = payConfig.get(SYSTEM_CONFIG_UNIONPAY_MER_ID);
        String txnAmt = String.valueOf( price);
        String orderId = rechargeOrderNo;
        String txnTime = DateUtil.getCurrTime();

        Map<String, String> contentData = new HashMap<String, String>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        contentData.put("version", "5.0.0");            //版本号 全渠道默认值
        contentData.put("encoding", "UTF-8");     //字符集编码 可以使用UTF-8,GBK两种方式
        contentData.put("signMethod", "01");           		 	//签名方法 目前只支持01：RSA方式证书加密
        contentData.put("txnType", "01");              		 	//交易类型 01:消费
        contentData.put("txnSubType", "01");           		 	//交易子类 01：消费
        contentData.put("bizType", "000201");          		 	//填写000201
        contentData.put("channelType", "08");          		 	//渠道类型 08手机

        /***商户接入参数***/
        contentData.put("merId", merId);   		 				//商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        contentData.put("accessType", "0");            		 	//接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
        contentData.put("orderId", orderId);        	 	    //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        contentData.put("txnTime", txnTime);		 		    //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        contentData.put("accType", "01");					 	//账号类型 01：银行卡02：存折03：IC卡帐号类型(卡介质)
        contentData.put("txnAmt", txnAmt);						//交易金额 单位为分，不能带小数点
        contentData.put("currencyCode", "156");                 //境内商户固定 156 人民币
        //contentData.put("reqReserved", "透传字段");              //商户自定义保留域，交易应答时会原样返回

        //后台通知地址（需设置为外网能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，【支付失败的交易银联不会发送后台通知】
        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200或302，那么银联会间隔一段时间再次发送。总共发送5次，银联后续间隔1、2、4、5 分钟后会再次通知。
        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        contentData.put("backUrl", payConfig.get(SYSTEM_CONFIG_UNIONPAY_NOTIFY_URL));

        /**对请求参数进行签名并发送http post请求，接收同步应答报文**/
        Map<String, String> reqData = AcpService.sign(contentData,"UTF-8");			 //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();								 //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> rspData = AcpService.post(reqData,requestAppUrl,"UTF-8");  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        String tn = null;
        if(!rspData.isEmpty()){
            if(AcpService.validate(rspData, "UTF-8")){
                LogUtil.writeLog("验证签名成功");
                String respCode = rspData.get("respCode") ;
                if(("00").equals(respCode)){
                    //成功,获取tn号
                    tn = rspData.get("tn");
                }
            }else{
                LogUtil.writeErrorLog("验证签名失败");
            }
        }else{
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
        }
        return tn;
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
        String encoding = params.get("encoding");
        String rechargeOrderNo = params.get("orderId"); //订单号
        String respCode = params.get("respCode");
        RechargeOrderExample example = new RechargeOrderExample();
        example.createCriteria().andRechargeOrderNoEqualTo(rechargeOrderNo);
        List<RechargeOrder> rechargeOrders = rechargeOrderMapper.selectByExample(example);
        if ( !"00".equals( respCode) || CollectionUtils.isEmpty( rechargeOrders)){
            return false;
        }
        RechargeOrder rechargeOrder = rechargeOrders.get( 0);
        String txnAmt = String.valueOf(rechargeOrder.getTotalAmount());//系统的充值金额
        Map<String, String> config = getUnionPayConfig();
        String merId = config.get(SYSTEM_CONFIG_UNIONPAY_MER_ID); //系统的商户号
        params.put( "txnAmt", new String(txnAmt.getBytes(encoding), encoding));
        params.put( "merId", new String(merId.getBytes(encoding), encoding));
        if (!AcpService.validate(params, encoding)) {
            LOGGER.error( "银联充值，验签失败。params:{}", JSON.toJSONString( params));
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
            rechargeParam.setOrderNo(rechargeOrderNo);
            //隐式加密
            TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(rechargeParam), TreeMap.class);
            String signKey = SignUtil.getSignKey(toSign);
            RpcContext.getContext().setAttachment("key", signKey);

            ResponseVo<PtbAccountVo> repsonseVO = accountApi.recharge(rechargeParam);
            if (repsonseVO == null || !"0".equals(repsonseVO.getCode())) {
                sendRetryMessage(rechargeParam);
            } else {
                LOGGER.info("充值订单号：{} 充值成功!", rechargeOrderNo);
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
            LOGGER.error("银联充值失败，放入消息队列重试,params:" + JSONObject.toJSONString(params));
        } finally {
            rechargeOrder.setStatus(RechargeOrderStatusEnum.paid.getRechargeOrderStatus());
            rechargeOrder.setPayTime(new Date());
            rechargeOrderMapper.updateByPrimaryKey(rechargeOrder);
        }
        return true;
    }

    private void sendRetryMessage(AccountRechargeParam rechargeParam) {
        AccountRechargeParamMessageVO messageVO = new AccountRechargeParamMessageVO();
        messageVO.setAccountRechargeParam(rechargeParam);
        LOGGER.info("发送重试充值消息：" + JSONObject.toJSONString(messageVO));
        busService.sendAccountRechargeRetryMessage(messageVO);
    }

    private LoadingCache<String, Map<String, String>> unionPayConfigCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES) //缓存10分钟
            .build(new CacheLoader<String, Map<String, String>>() {
                @Override
                public Map<String, String> load(String s) {
                    List<String> params = new ArrayList<String>();
                    params.add(SYSTEM_CONFIG_UNIONPAY_MER_ID);
                    params.add(SYSTEM_CONFIG_UNIONPAY_NOTIFY_URL);
                    ResponseVo<Map<String, String>> result = systemConfigApi.getConfigs(params);
                    if ( !"0".equals( result.getCode())) {
                        return new HashMap<>();
                    }
                    return result.getData();
                }
            });

    public Map<String, String> getUnionPayConfig() {
        Map<String, String> map = new HashMap<>();
        try {
            map = unionPayConfigCache.get("ptb.pay.unionpay.config");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return map;
    }
}
