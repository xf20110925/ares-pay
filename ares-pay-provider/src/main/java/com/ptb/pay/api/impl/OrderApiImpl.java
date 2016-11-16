package com.ptb.pay.api.impl;

import com.ptb.account.api.IAccountApi;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IOrderApi;
import com.ptb.pay.enums.ErrorCode;
import com.ptb.utils.service.ReturnUtil;
import com.ptb.utils.tool.ParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zuokui.fu on 2016/11/16.
 */
public class OrderApiImpl implements IOrderApi {

    private static final Logger logger = LoggerFactory.getLogger( OrderApiImpl.class);

    @Autowired
    private IAccountApi accountApi;

    @Override
    public ResponseVo cancelApplyRefund(String orderNo) {
        return null;
    }

    @Transactional( rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ResponseVo agreeRefund(String orderNo, Long money, String deviceType) {
        //参数校验
        if ( !ParamUtil.checkParams( orderNo, money, deviceType)){
            return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1001.getCode(), ErrorCode.PAY_API_COMMMON_1001.getMessage());
        }
        if ( money <= 0){
            return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1002.getCode(), ErrorCode.PAY_API_COMMMON_1002.getMessage());
        }
        //检查订单状态

        //检查订单应付款金额

        //更新订单状态

        //新增订单日志记录

        //调用虚拟账户退款接口

        return null;
    }
}
