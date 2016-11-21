package com.ptb.pay.api;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.vo.order.ConfirmOrderReqVO;
import com.ptb.pay.vo.order.OrderListReqVO;
import com.ptb.pay.vo.order.OrderListVO;

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
    ResponseVo buyerPayment(Long userId, Long orderId,String plyPassword,String deviceType)throws  Exception;

    /**
     * 买家申请退款
     * @param userId
     * @param orderId
     * @param deviceType
     * @return
     * @throws Exception
     */
    ResponseVo refund(Long userId,Long orderId,String deviceType)throws  Exception;

    /**
     * 买家提交订单
     * @param userId
     * @param productId
     * @param desc
     * @param device
     * @return
     */
    ResponseVo submitOrder(long userId, long productId, String desc, int device);

    /**
     * 买家取消订单
     * @param userId
     * @param orderId
     * @return
     */
    ResponseVo cancelOrder(long userId, long orderId);

    /**
     * 获取订单详情
     * @param userId
     * @param orderNo
     * @return
     */
    ResponseVo getOrderDetail(long userId, String orderNo);

    /**
     * 买卖双方确认完成接口
     * @param userId
     * @param confirmOrderVO
     * @return
     */
    public ResponseVo confirmOrder(long userId, ConfirmOrderReqVO confirmOrderVO);

    /**
     * 获取订单列表接口
     * @param userId
     * @param orderListReqVO
     * @return
     */
    public ResponseVo<OrderListVO> getOrderList(long userId, OrderListReqVO orderListReqVO);
}
