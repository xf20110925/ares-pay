package com.ptb.pay.api;

import com.ptb.pay.vo.response.ResponseVo;

import javax.xml.ws.Response;

/**
 * Created by zuokui.fu on 2016/10/18.
 */
public interface ITestApi {

    public ResponseVo<Object> test();
}
