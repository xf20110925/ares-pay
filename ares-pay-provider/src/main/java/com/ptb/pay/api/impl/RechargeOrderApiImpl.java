package com.ptb.pay.api.impl;

import com.ptb.common.enums.RechargeOrderStatusEnum;
import com.ptb.common.errorcode.CommonErrorCode;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IRechargeOrderApi;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.model.RechargeOrderExample;
import com.ptb.pay.service.interfaces.IRechargeOrderService;
import com.ptb.pay.service.factory.RechargeOrderServiceFactory;
import com.ptb.utils.service.ReturnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.ptb.pay.vo.RechargeOrderParamsVO;
import com.ptb.pay.vo.RechargeOrderVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: 充值订单API实现
 * All Rights Reserved.
 * @version 1.0  2016-11-15 10:35 by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Component("rechargeOrderApi")
public class RechargeOrderApiImpl implements IRechargeOrderApi {

    private static Logger logger = LoggerFactory.getLogger( RechargeOrderApiImpl.class);

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Override
    public ResponseVo<Map<String, Object>> createRechargeOrder(RechargeOrderParamsVO paramsVO) throws Exception {
        IRechargeOrderService rechargeOrderService = RechargeOrderServiceFactory.createService(paramsVO.getPayMethod());
        RechargeOrder rechargeOrder = rechargeOrderService.createRechargeOrder(paramsVO);
        return ReturnUtil.success(rechargeOrderService.getReturnData(rechargeOrder));
    }

    @Override
    public ResponseVo<List<RechargeOrderVO>> getRechargeOrderList(Long userId) throws Exception {
        if(userId == null){
            return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_ARGSERROR.getCode(),
                    CommonErrorCode.COMMMON_ERROR_ARGSERROR.getMessage());
        }

        RechargeOrderExample example = new RechargeOrderExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<Integer> status = new ArrayList<Integer>();
        status.add(RechargeOrderStatusEnum.paid.getRechargeOrderStatus());
        status.add(RechargeOrderStatusEnum.review.getRechargeOrderStatus());
        example.createCriteria().andStatusIn(status);
        example.setOrderByClause("create_time desc");
        List<RechargeOrder> orders = rechargeOrderMapper.selectByExample(example);
        List<RechargeOrderVO> returnData = new ArrayList<RechargeOrderVO>();
        if(CollectionUtils.isEmpty(orders)){
            return ReturnUtil.success(returnData);
        }
        for (RechargeOrder order : orders) {
            RechargeOrderVO orderVO = new RechargeOrderVO();
            orderVO.setPayTime(order.getPayTime());
            orderVO.setPayType(order.getPayType());
            orderVO.setCreateTime(order.getCreateTime());
            orderVO.setUserId(order.getUserId());
            orderVO.setDeviceType(order.getDeviceType());
            orderVO.setOrderId(order.getPtbRechargeOrderId());
            orderVO.setRechargeAmount(order.getTotalAmount());
            orderVO.setStatus(order.getStatus());
            orderVO.setVerificationCode(order.getVerificationCode());
            orderVO.setPayMethod(order.getPayMethod());
            orderVO.setRechargeOrderNo(order.getRechargeOrderNo());
            returnData.add(orderVO);
        }
        return ReturnUtil.success(returnData);
    }
}
