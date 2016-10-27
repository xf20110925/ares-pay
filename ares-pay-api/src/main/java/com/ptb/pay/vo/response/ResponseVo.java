package com.ptb.pay.vo.response;

import java.io.Serializable;

/**
 * Created by zuokui.fu on 2016/10/27.
 */
public class ResponseVo<T> implements Serializable {

    private String code;       //返回码。0代表调用成功，非0代表具体的错误码
    private String message;    //返回信息。
    private T data;            //返回数据。

    public ResponseVo( String code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
