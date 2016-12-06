package com.ptb.pay.utils.wxpay;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Created by zuokui.fu on 2016/12/6.
 */
public class RequestHandler {

    private String charset;
    private String apiKey;

    public RequestHandler( String apiKey){
        this.apiKey = apiKey;
        this.charset = "UTF-8";
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public String createSign(SortedMap<String, String> packageParams) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + this.getApiKey());
        //System.out.println("md5 sb:" + sb);
        String sign = MD5Util.MD5Encode(sb.toString(), this.charset)
                .toUpperCase();
        //System.out.println("packge签名:" + sign);
        return sign;

    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
