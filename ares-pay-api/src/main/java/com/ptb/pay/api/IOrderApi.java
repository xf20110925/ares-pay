package com.ptb.pay.api;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.vo.product.ProductVO;

import java.util.Map;

/**
 * 订单相关的接口服务
 * Created by zuokui.fu on 2016/11/16.
 */
public interface IOrderApi {

    /**
     * 买家取消申请退款
     * @param orderId 订单主键
     * @return
     */
    ResponseVo<Map<String,Object>> cancelApplyRefund( Long buyerId, Long orderId) throws Exception;

    /**
     * 卖家同意退款
     * @param salerId 卖家用户编号
     * @param orderId 订单主键
     * @param money 退款金额
     * @param deviceType 设备类型
     * @return
     */
    ResponseVo<Map<String,Object>> agreeRefund(Long salerId, Long orderId, Long money, String deviceType) throws Exception;

    /**
     * 买家付款
     * @param userId
     * @param orderId
     * @param plyPassword
     * @return
     */
    ResponseVo buyerPayment(long userId, String orderId,String plyPassword);


    /**
     * 用户提交订单
     * @param userId
     * @return
     */
    ResponseVo submitOrder(long userId, ProductVO productVO);
}
