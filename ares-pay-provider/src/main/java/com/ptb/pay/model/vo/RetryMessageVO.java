package com.ptb.pay.model.vo;

/**
 * Created by MyThinkpad on 2016/12/17.
 */
public class RetryMessageVO<T,B> {
    private T type;
    private String title;
    private String message;
    private int sendTimes = 0;
    private B body;

    public RetryMessageVO(){}

    public RetryMessageVO(T type, String title, String message, B body){
        this.type = type;
        this.title = title;
        this.message = message;
        this.body = body;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public B getBody() {
        return body;
    }

    public void setBody(B body) {
        this.body = body;
    }

    public int getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(int sendTimes) {
        this.sendTimes = sendTimes;
    }
}
