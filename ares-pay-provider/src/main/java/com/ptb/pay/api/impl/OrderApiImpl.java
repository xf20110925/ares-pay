package com.ptb.pay.api.impl;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.ptb.account.api.IAccountApi;
import com.ptb.account.vo.PtbAccountVo;
import com.ptb.account.vo.param.AccountPayParam;
import com.ptb.account.vo.param.AccountRefundParam;
import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.PlatformEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IOrderApi;
import com.ptb.pay.api.IProductApi;
import com.ptb.pay.enums.*;
import com.ptb.pay.mapper.impl.OrderMapper;
import com.ptb.pay.mapper.impl.ProductMapper;
import com.ptb.pay.model.Order;
import com.ptb.pay.model.Product;
import com.ptb.pay.model.order.OrderDetail;
import com.ptb.pay.service.interfaces.IOrderDetailService;
import com.ptb.pay.service.interfaces.IOrderService;
import com.ptb.pay.vo.order.*;
import com.ptb.pay.vo.product.ProductVO;
import com.ptb.pay.vopo.ConvertOrderUtil;
import com.ptb.pay.vopo.ConvertProductUtil;
import com.ptb.ucenter.api.IBindMediaApi;
import com.ptb.pay.vo.order.OrderDetailVO;
import com.ptb.utils.encrypt.SignUtil;
import com.ptb.utils.service.ReturnUtil;
import com.ptb.utils.tool.GenerateOrderNoUtil;
import com.ptb.utils.tool.ParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * Created by zuokui.fu on 2016/11/16.
 */
@Service("orderApi")
public class OrderApiImpl implements IOrderApi {

    private static final Logger logger = LoggerFactory.getLogger( OrderApiImpl.class);

    @Autowired
    private IAccountApi accountApi;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IProductApi productApi;
    @Autowired
    private IBindMediaApi bindMediaApi;

    @Transactional( rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ResponseVo<Map<String,Object>> cancelApplyRefund(Long buyerId, Long orderId) throws Exception {
        logger.info( "卖家取消申请退款。buyerId:{} orderId:{}", buyerId, orderId);
        try {
            //参数校验
            if (!ParamUtil.checkParams(buyerId, orderId)) {
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1001.getCode(), ErrorCode.PAY_API_COMMMON_1001.getMessage());
            }
            //检查订单状态
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (null!= order && order.getBuyerId().longValue() != buyerId.longValue()) {
                //买家ID与订单中的买家ID不符
                return ReturnUtil.error(ErrorCode.ORDER_API_5004.getCode(), ErrorCode.ORDER_API_5004.getMessage());
            }
            if (!orderService.checkOrderStatus(OrderActionEnum.BUYER_CANCEL_REFUND, order.getOrderStatus(), order.getSellerStatus(), order.getBuyerStatus())) {
                //订单状态有误
                return ReturnUtil.error(ErrorCode.ORDER_API_5002.getCode(), ErrorCode.ORDER_API_5002.getMessage());
            }
            //更新订单状态、新增订单日志记录
            orderService.updateStatusForCancelRefund( orderId, buyerId, order.getOrderNo());
            Order resultOrder = orderMapper.selectByPrimaryKey( orderId);
            return ReturnUtil.success( orderService.getBuyerOrderStatus( resultOrder.getOrderStatus().toString()+resultOrder.getSellerStatus()+resultOrder.getBuyerStatus()));
        } catch ( Exception e){
            logger.error( "买家取消申请退款接口调用失败。error message: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional( rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ResponseVo<Map<String,Object>> agreeRefund(Long salerId, Long orderId, Long money, String deviceType) throws Exception {
        logger.info( "卖家同意退款。salerId:{} orderId:{}, money:{}, deviceType:{}", salerId, orderId, money, deviceType);
        try {
            //参数校验
            if (!ParamUtil.checkParams(salerId, orderId, money, deviceType)) {
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1001.getCode(), ErrorCode.PAY_API_COMMMON_1001.getMessage());
            }
            if (money <= 0) {
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1002.getCode(), ErrorCode.PAY_API_COMMMON_1002.getMessage());
            }
            //检查订单状态
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (null!= order && order.getSellerId().longValue() != salerId.longValue()) {
                //卖家ID与订单中的卖家ID不符
                return ReturnUtil.error(ErrorCode.ORDER_API_5001.getCode(), ErrorCode.ORDER_API_5001.getMessage());
            }
            if (!orderService.checkOrderStatus(OrderActionEnum.SALER_AGREE_REFUND, order.getOrderStatus(), order.getSellerStatus(), order.getBuyerStatus())) {
                //订单状态有误
                return ReturnUtil.error(ErrorCode.ORDER_API_5002.getCode(), ErrorCode.ORDER_API_5002.getMessage());
            }
            //检查订单应付款金额
            if (money.longValue() != order.getPayablePrice()) {
                //退款金额有误
                return ReturnUtil.error(ErrorCode.ORDER_API_5003.getCode(), ErrorCode.ORDER_API_5003.getMessage());
            }
            //更新订单状态、新增订单日志记录
            orderService.updateStatusForArgeeRefund( orderId, salerId, order.getOrderNo());
            //调用虚拟账户退款接口
            AccountRefundParam param = new AccountRefundParam();
            param.setBuyerId(order.getBuyerId());
            param.setSalerId(order.getSellerId());
            param.setMoney(money);
            param.setOrderNo( order.getOrderNo());
            param.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(deviceType));
            param.setPlatform(PlatformEnum.xiaomi);
            //隐式加密
            TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(param), TreeMap.class);
            String sign = SignUtil.getSignKey(toSign);
            RpcContext.getContext().setAttachment("key", sign);
            ResponseVo<PtbAccountVo> accountResponse = accountApi.refund(param);
            if ( !"0".equals( accountResponse.getCode())){
                logger.error( "虚拟账户退款dubbo接口调用失败。salerId:{}", salerId);
                throw new Exception();
            }
            Order resultOrder = orderMapper.selectByPrimaryKey( orderId);
            return ReturnUtil.success( orderService.getSalerOrderStatus( resultOrder.getOrderStatus().toString()+resultOrder.getSellerStatus()+resultOrder.getBuyerStatus()));
        } catch ( Exception e){
            logger.error( "卖家同意退款接口调用失败。error message: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional( rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ResponseVo buyerPayment(Long userId, Long orderId, String plyPassword,String deviceType) throws Exception {
        logger.info( "买家开始付款。userId:{} orderId:{}, plyPassword:{}", userId, orderId,plyPassword);
        try{
            //参数校验
            if (!ParamUtil.checkParams(userId,orderId,plyPassword)){
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1001.getCode(), ErrorCode.PAY_API_COMMMON_1001.getMessage());
            }
            //查询订单信息
            Order order = orderMapper.selectByPrimaryKey(orderId);
            //检查订单是否有误
            if (null!= order && order.getBuyerId().longValue() != userId.longValue()){
                return ReturnUtil.error(ErrorCode.ORDER_API_5001.getCode(), ErrorCode.ORDER_API_5001.getMessage());
            }
            if (!orderService.checkOrderStatus(OrderActionEnum.BUYER_PAY, order.getOrderStatus(), order.getSellerStatus(), order.getBuyerStatus())) {
                //订单状态有误
                return ReturnUtil.error(ErrorCode.ORDER_API_5002.getCode(), ErrorCode.ORDER_API_5002.getMessage());
            }
            //更新订单状态并增加订单日志
            orderService.updateStatusBuyerPayment(order.getPtbOrderId(),userId,order.getOrderNo());

            //调用dubbo付款消费虚拟币
            AccountPayParam param = new AccountPayParam();
            param.setBuyerId(order.getBuyerId());
            param.setSalerId(order.getSellerId());
            param.setMoney(order.getPayablePrice());
            param.setOrderNo(order.getOrderNo());
            param.setPayPassword(plyPassword);
            param.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(deviceType));
            param.setPlatform(PlatformEnum.xiaomi);
            //隐式加密
            TreeMap toSign = JSONObject.parseObject(JSONObject.toJSONString(param), TreeMap.class);
            String sign = SignUtil.getSignKey(toSign);
            RpcContext.getContext().setAttachment("key", sign);
            ResponseVo<PtbAccountVo> responseVo = accountApi.pay(param);
            if (responseVo.getCode().equals("6002"))return responseVo;
            if ( !"0".equals( responseVo.getCode())){
                logger.error( "虚拟账户付款dubbo接口调用失败。salerId:{}", userId);
                throw new Exception();
            }
            Order resultOrder = orderMapper.selectByPrimaryKey(orderId);
            Map<String, Object> buyerOrderStatus = orderService.getBuyerOrderStatus(resultOrder.getOrderStatus().toString() + resultOrder.getSellerStatus() + resultOrder.getBuyerStatus());
            ResponseVo responseVo1 = ReturnUtil.success("操作成功", buyerOrderStatus);
            return responseVo1;
        }catch (Exception e){
            logger.error( "买家付款接口调用失败。error message: {}", e.getMessage());
            throw e;
        }
    }
    @Transactional( rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ResponseVo refund(Long userId, Long orderId, String deviceType) throws Exception {
        logger.info( "买家申请退款。userId:{} orderId:{}", userId, orderId);
        try {
            //参数校验
            if (!ParamUtil.checkParams(userId,orderId)){
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1001.getCode(), ErrorCode.PAY_API_COMMMON_1001.getMessage());
            }
            //检查订单状态
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (null!= order && order.getBuyerId().longValue() != userId.longValue()) {
                //买家ID与订单中的买家ID不符
                return ReturnUtil.error(ErrorCode.ORDER_API_5004.getCode(), ErrorCode.ORDER_API_5004.getMessage());
            }
            if (!orderService.checkOrderStatus(OrderActionEnum.BUYER_APPLY_REFUND, order.getOrderStatus(), order.getSellerStatus(), order.getBuyerStatus())) {
                //订单状态有误
                return ReturnUtil.error(ErrorCode.ORDER_API_5002.getCode(), ErrorCode.ORDER_API_5002.getMessage());
            }
            //更新订单状态、并新增订单日志记录
            orderService.updateStaterefund(orderId,userId, order.getOrderNo());
            Order resultOrder = orderMapper.selectByPrimaryKey(orderId);
            Map<String, Object> buyerOrderStatus = orderService.getBuyerOrderStatus(resultOrder.getOrderStatus().toString() + resultOrder.getSellerStatus() + resultOrder.getBuyerStatus());
            ResponseVo responseVo = ReturnUtil.success("操作成功", buyerOrderStatus);
            return responseVo;
        }catch (Exception e){
            logger.error( "买家申请退款接口调用失败。error message: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional (rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    @Override
    public ResponseVo submitOrder(long userId, long productId, String desc, int device) {
        logger.info("买家提交订单 userID:{}", userId);

        try {
            //查询商品信息
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product == null) {
                return ReturnUtil.error("20002", "no product");
            }
            //生成订单号
            String orderNo = GenerateOrderNoUtil.createOrderNo(2, device, 3);
            if (orderNo == null){
                logger.error("generate order no error! userId:{} productId:{}", userId, product.getPtbProductId());
                return ReturnUtil.error("20002", "no product");
            }
            //添加新订单
            Order order = orderService.insertNewOrder(userId, product.getOwnerId(), product.getPrice(), orderNo, desc);
            String json = JSON.json(order);
            OrderVO orderVO = JSON.parse(json, OrderVO.class);

            //添加订单详情
            OrderDetailVO orderDetailVO = orderDetailService.convertOrderDetailVO(orderNo, product.getPrice(), 0, product.getPtbProductId());
            int insert = orderDetailService.insertOrderDetail(orderDetailVO);
            if (insert < 1){
                logger.error("insert order detail error! orderNo:{} product iD:{}", orderNo, product.getPtbProductId());
                throw new RuntimeException("order detail error!");
            }

            Map<String, Object> map = orderService.getBuyerOrderStatus( ""+order.getOrderStatus()+order.getSellerStatus()+order.getBuyerStatus());
            if (map.get("button") != null){
                orderVO.setButton(map.get("button").toString());
            }else {
                orderVO.setButton(null);
            }
            if (map.get("desc") != null){
                orderVO.setDesc(map.get("desc").toString());
            }else {
                orderVO.setDesc(null);
            }
            return new ResponseVo<OrderVO>("0", "", orderVO);
        }catch (Exception e){
            logger.error("submit order error!", e);
            return ReturnUtil.error("20002", "no product");
        }
    }

    @Override
    public ResponseVo<BaseOrderResVO> cancelOrder(long userId, long orderId) {
        logger.info("买家取消订单 buyerId:{}  orderId:{}", userId, orderId);
        //是否可以取消订单
        Order orderByOrderId = orderService.getOrderByOrderId(orderId);
        if (orderByOrderId.getOrderStatus() != 0){
            logger.warn("can not cancel order orderId:{}", orderId);
            return ReturnUtil.error("41003","can not cancel order");
        }
        if (orderByOrderId.getBuyerId() != userId){
            logger.warn("buyerId is not match orderId:{},userId:{}", orderId,userId);
            return ReturnUtil.error("41003","can not cancel order");
        }
        //修改订单状态
        Order order = null;
        try {
            order = orderService.cancelOrderByBuyer(userId, orderId);
            if (order == null){
                throw new Exception("取消订单失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.error("30010","取消订单失败");
        }

        BaseOrderResVO baseOrderResVO = new BaseOrderResVO();
        Map<String, Object> map = orderService.getBuyerOrderStatus( ""+order.getOrderStatus()+order.getSellerStatus()+order.getBuyerStatus());
        if (map.get("button") != null){
            baseOrderResVO.setButton(map.get("button").toString());
        }else {
            baseOrderResVO.setButton(null);
        }
        if (map.get("desc") != null){
            baseOrderResVO.setDesc(map.get("desc").toString());
        }else {
            baseOrderResVO.setDesc(null);
        }
        return new ResponseVo("0", "", baseOrderResVO);
    }

    @Override
    public ResponseVo getOrderDetail(long userId, String orderNo) {
        OrderDetailVO orderDetail = orderDetailService.getOrderDetail(orderNo);
        if (orderDetail == null){
            logger.error("获取订单详情失败 userId:{} orderNo:{}", userId, orderNo);
            return ReturnUtil.error("20001", "获取订单详情失败");
        }
        return new ResponseVo("0", "获取订单详情成功", orderDetail);
    }

    @Override
    public ResponseVo getOrderInfo(long userId, Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(null == order || (userId != order.getBuyerId() && userId != order.getSellerId()))
            return ReturnUtil.error(ErrorCode.ORDER_API_5005.getCode(), ErrorCode.ORDER_API_5005.getMessage());
        OrderVO orderVO = ConvertOrderUtil.convertOrderToOrderVO(order);

        Map<String,Object> map = null;
        if(userId == order.getBuyerId())
            map = orderService.getBuyerOrderStatus("" +order.getOrderStatus() + order.getSellerStatus() + order.getBuyerStatus());
        else
            map = orderService.getSalerOrderStatus("" +order.getOrderStatus() + order.getSellerStatus() + order.getBuyerStatus());

        if (map.get("button") != null){
            orderVO.setButton(map.get("button").toString());
        }else {
            orderVO.setButton(null);
        }
        if (map.get("desc") != null){
            orderVO.setDesc(map.get("desc").toString());
        }else {
            orderVO.setDesc(null);
        }
        OrderDetailVO orderDetail = orderDetailService.getOrderDetail(order.getOrderNo());
        ResponseVo<ProductVO> responseVo = productApi.getProduct(userId, orderDetail.getProductId());
        orderVO.setProductVOList(singletonList(responseVo.getData()));
        return ReturnUtil.success(orderVO);
    }

    @Override
    public ResponseVo sellerChangePrice(long userId, long orderId, long price) {
        //是否可以修改
        Order orderByOrderId = orderService.getOrderByOrderId(orderId);
        if (orderByOrderId == null){
            logger.error("get order by orderId error! orderId:{}", orderId);
            return ReturnUtil.error("30001","获取订单信息出错!");
        }
        if (orderByOrderId.getOrderStatus() != 0 || orderByOrderId.getBuyerStatus() != 0 || orderByOrderId.getSellerStatus() != 0){
            logger.warn("[ERROR] 用户不能修改价格! userId:{} orderId:{}",userId, orderId);
            return ReturnUtil.error("30002","[ERROR] 用户不能修改价格!");
        }
        if (orderByOrderId.getSellerId() != userId){
            logger.warn("[ERROR] 卖家ID不匹配! userId:{} orderId:{}",userId, orderId);
            return ReturnUtil.error("30002","[ERROR] 用户不能修改价格!");
        }
        //修改订单价格
        int update = orderService.changeOrderPrice(userId, orderId, price);
        if (update < 1){
            logger.error("卖家更新价格出错!");
        }
        return new ResponseVo("0","更新成功",null);
    }

    @Override
    @Transactional
    public ResponseVo confirmOrder(long userId, ConfirmOrderReqVO confirmOrderVO){
        //参数校验
        Order order = orderMapper.selectByPrimaryKey(confirmOrderVO.getOrderId());
        if(null == order)
            return ReturnUtil.error(ErrorCode.ORDER_API_5005.getCode(), ErrorCode.ORDER_API_5005.getMessage());

        if(confirmOrderVO.getUserType() == UserType.USER_IS_SELLER.getUserType()){ //当前用户是卖家


            //用户与订单是否匹配
            if(userId != order.getSellerId())
                return ReturnUtil.error(ErrorCode.ORDER_API_5001.getCode(), ErrorCode.ORDER_API_5001.getMessage());

            //卖家确认
            if(orderService.checkOrderStatus(OrderActionEnum.SALER_COMPLETE, order.getOrderStatus(),order.getSellerStatus(), order.getBuyerStatus())){
                //修改相关状态
                boolean ret = orderService.sellerConfirmOrder(userId, order);
                return ret?
                        ReturnUtil.success(orderService.getSalerOrderStatus( ""+order.getOrderStatus()+order.getSellerStatus()+order.getBuyerStatus())):
                        ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1000.getCode(),ErrorCode.PAY_API_COMMMON_1000.getMessage());
            }else{
                //买家未付款, 操作失败
                return ReturnUtil.error(ErrorCode.ORDER_API_5002.getCode(), ErrorCode.ORDER_API_5002.getMessage());
            }


        }else if(confirmOrderVO.getUserType() == UserType.USER_IS_BUYER.getUserType()){ //当前用户是买家

            //用户与订单是否匹配
            if(userId != order.getBuyerId())
                return ReturnUtil.error(ErrorCode.ORDER_API_5004.getCode(), ErrorCode.ORDER_API_5004.getMessage());

            //密码不能为空
            if(confirmOrderVO.getPassword() == null){
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1001.getCode(), ErrorCode.PAY_API_COMMMON_1001.getMessage());
            }
            if(!orderService.checkOrderStatus(OrderActionEnum.SALER_COMPLETE, order.getOrderStatus(),order.getSellerStatus(), order.getBuyerStatus())){
                return ReturnUtil.error(ErrorCode.ORDER_API_5002.getCode(), ErrorCode.ORDER_API_5002.getMessage());
            }

            ResponseVo responseVo = null;
            //取消款项冻结
            try {
                responseVo = buyerPayment(userId, order.getPtbOrderId(), confirmOrderVO.getPassword(), confirmOrderVO.getDeviceTypeEnum().getDeviceType());
                if(!responseVo.getCode().equals("0")) {
                    return ReturnUtil.error(responseVo.getCode(), responseVo.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1000.getCode(), ErrorCode.PAY_API_COMMMON_1000.getMessage());
            }

            try {
                //上报用户中心交易成功
                ResponseVo responseVo1 = bindMediaApi.reportDealInfo(order.getSellerId(), order.getBuyerId(), 0);
                if(!responseVo1.getCode().equals("0")){
                    //更新失败 add message to bus
                }
                //更新商品计数
                Long productId = orderDetailService.getProductIdByOrderNo(order.getOrderNo());
                if(productId != null) {
                    responseVo1 = productApi.updateProductDealNum(userId, productId);
                    if (!responseVo1.getCode().equals("0")) {
                        //更新失败 add message to bus
                        logger.error("productDealNum update error orderNo:" + order.getOrderNo() + " productId:" + productId);
                    }
                }else{
                    logger.error("orderNo " + order.getOrderNo() + " not exists");
                }
                //更新订单状态 与 订单操作日志
                boolean ret = orderService.buyerConfirmOrder(userId, order);
                if(!ret){
                    //更新失败 add message to bus
                    logger.error("order info and order log info update error orderNo:" + order.getOrderNo());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ReturnUtil.success(orderService.getBuyerOrderStatus(""+order.getOrderStatus()+order.getSellerStatus()+order.getBuyerStatus()));
        }else{
            return ReturnUtil.error("","未知用户类型");
        }
    }

    @Override
    public ResponseVo<OrderListVO> getOrderList(long userId, OrderListReqVO orderListReqVO){
        OrderListVO orderListVO = OrderListVO.Empty();
        //用户校验
        if(userId != orderListReqVO.getUserId())
            return ReturnUtil.success(orderListVO);

        int userType = UserType.USER_IS_BUYER.getUserType();
        //获取用户所有订单
        List<Order> orders = null;
        if(orderListReqVO.getUserType() == UserType.USER_IS_BUYER.getUserType())
            orders = orderMapper.selectByBuyerUid(orderListReqVO.getUserId());
        else {
            userType = UserType.USER_IS_SELLER.getUserType();
            orders = orderMapper.selectBySellerUid(orderListReqVO.getUserId());
        }

        //订单为空返回
        if(CollectionUtils.isEmpty(orders))
            return ReturnUtil.success(orderListVO);

        //状态过滤
        if(orderListReqVO.getOrderStatus() != OrderStatusEnum.ORDER_STATUS_DEAL_ALL.getStatus()){
            orders = orders.stream().filter(item->item.getOrderStatus()==orderListReqVO.getOrderStatus()).collect(Collectors.toList());
        }

        //分页 转换
        orderListVO.setTotalNum(orders.size());
        orderListVO.getOrderVOList().addAll(orders.stream().map(ConvertOrderUtil::convertOrderToOrderVO).skip(orderListReqVO.getStart()).limit(orderListReqVO.getEnd()-orderListReqVO.getStart()).collect(Collectors.toList()));
        final int finalUserType = userType;
        List<String> orderNoList = new ArrayList<>();
        orderListVO.getOrderVOList().forEach(item->{
            Map<String, Object> map = finalUserType ==UserType.USER_IS_SELLER.getUserType()?orderService.getSalerOrderStatus( ""+item.getOrderStatus()+item.getSellerStatus()+item.getBuyerStatus()):orderService.getBuyerOrderStatus(""+item.getOrderStatus()+item.getSellerStatus()+item.getBuyerStatus());
            if (map.get("button") != null){
                item.setButton(map.get("button").toString());
            }else {
                item.setButton(null);
            }
            if (map.get("desc") != null){
                item.setDesc(map.get("desc").toString());
            }else {
                item.setDesc(null);
            }
            orderNoList.add(item.getOrderNo());
        });

        //获取订单对应商品列表
        if(!orderNoList.isEmpty()){
            List<OrderDetail> details = orderDetailService.getOrderDetailList(orderNoList);
            Map<String, OrderDetail> ordrNoMapProductId = new HashMap<>();//details.stream().collect(Collectors.toMap(OrderDetail::getOrderNo,(k)->k));
            details.forEach(item->{
                ordrNoMapProductId.put(item.getOrderNo(), item);
            });
            List<Long> pidlist = details.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
            if(!pidlist.isEmpty()) {
                Map<Long, ProductVO> productIdMapProductVO = productMapper.selectByPtbProductID(pidlist).stream().map(ConvertProductUtil::convertProductToProductVO).collect(Collectors.toMap(ProductVO::getProductId, (k) -> k));
                orderListVO.getOrderVOList().forEach(item -> {
                    OrderDetail orderDetail = ordrNoMapProductId.get(item.getOrderNo());
                    if (null == orderDetail) {
                        logger.error("orderNo no detial " + item.getOrderNo());
                        return;
                    }
                    ProductVO productVO = productIdMapProductVO.get(orderDetail.getProductId());
                    if (null == productVO) {
                        logger.error("orderNo no product " + item.getOrderNo());
                        return;
                    }
                    item.setProductVOList(Collections.singletonList(productVO));
                });
            }
        }
        return ReturnUtil.success(orderListVO);
    }

}
