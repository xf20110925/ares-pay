package com.ptb.pay.util;

import com.ptb.pay.vo.response.ResponseVo;

/**
 * Created by zuokui.fu on 2016/10/27.
 */
public class ReturnUtil {

    public static final String successCode = "0";
    public static final String successMessage = "调用成功";


    public static ResponseVo success(){
        return success( successCode, successMessage, null);
    }

    public static ResponseVo success( Object data){
        return success( successCode, successMessage, data);
    }

    public static ResponseVo success( String message, Object data){
        return success( successCode, message, data);
    }

    private static ResponseVo<Object> success( String code, String message, Object data){
        return new ResponseVo( code, message, data);
    }

    public static ResponseVo error( String code, String message, Object data){
        return new ResponseVo( code, message, data);
    }
}
