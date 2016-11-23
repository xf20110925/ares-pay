package com.ptb.pay.api.impl;

import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.PlatformEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.BaseTest;
import com.ptb.pay.api.IOrderApi;
import com.ptb.pay.enums.OrderStatusEnum;
import com.ptb.pay.enums.UserType;
import com.ptb.pay.vo.order.ConfirmOrderReqVO;
import com.ptb.pay.vo.order.OrderListReqVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zuokui.fu on 2016/11/18.
 */
public class OrderApiImplTest extends BaseTest {

    @Autowired
    private IOrderApi orderApi;

    @Test
    public void cancelApplyRefund(){
        try {
            orderApi.cancelApplyRefund( 777L, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void agreeRefund(){
        try {
            orderApi.agreeRefund( 776L, 1L, 800000L, "iphone");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void confirmOrder(){

    }

    @Test
    public void getOrderList(){
        //所有订单
        OrderListReqVO orderListReqVO = new OrderListReqVO();
        orderListReqVO.setStart(0);
        orderListReqVO.setEnd(10);
        orderListReqVO.setUserId(517);
        orderListReqVO.setUserType(UserType.USER_IS_SELLER.getUserType());
        orderListReqVO.setDeviceTypeEnum(DeviceTypeEnum.android);
        orderListReqVO.setOrderStatus(OrderStatusEnum.ORDER_STATUS_NEW_DEAL.getStatus());
        orderApi.getOrderList(517, orderListReqVO);

/*        orderListReqVO.setOrderStatus(OrderStatusEnum.ORDER_STATUS_NEW_DEAL.getStatus());
        orderApi.getOrderList(777, orderListReqVO);

        orderListReqVO.setOrderStatus(OrderStatusEnum.ORDER_STATUS_DEALING.getStatus());
        orderApi.getOrderList(777, orderListReqVO);

        orderListReqVO.setOrderStatus(OrderStatusEnum.ORDER_STATUS_DEAL_ALL.getStatus());
        orderApi.getOrderList(777, orderListReqVO);*/

    }

    @Test
    public void getMediaDetailInfo(){
        orderApi.getOrderInfo(517, 124L);
    }

    @Test
    public void submitOrderTest(){
        try {
            orderApi.submitOrder(112, 41, "test", 1);
        }catch (Exception e){

        }
    }

    @Test
    public void cancelOrderTest(){
        try {
            ResponseVo responseVo = orderApi.cancelOrder(777l, 16);
            System.out.println(responseVo.getMessage());
        }catch (Exception e){

        }
    }

    @Test
    public void sellerConfirmOrder(){
        ConfirmOrderReqVO confirmOrderReqVO = new ConfirmOrderReqVO();
        confirmOrderReqVO.setOrderId(5);
        confirmOrderReqVO.setUserId(777);
        confirmOrderReqVO.setUserType(UserType.USER_IS_SELLER.getUserType());
        confirmOrderReqVO.setDeviceTypeEnum(DeviceTypeEnum.android);
        confirmOrderReqVO.setPassword("123123123");
        orderApi.confirmOrder(777, confirmOrderReqVO);
    }

    @Test
    public void buyerConfirmOrder(){
        ConfirmOrderReqVO confirmOrderReqVO = new ConfirmOrderReqVO();
        confirmOrderReqVO.setOrderId(5);
        confirmOrderReqVO.setUserId(1111);
        confirmOrderReqVO.setUserType(UserType.USER_IS_BUYER.getUserType());
        confirmOrderReqVO.setDeviceTypeEnum(DeviceTypeEnum.android);
        confirmOrderReqVO.setPlatformEnum(PlatformEnum.xiaomi);
        confirmOrderReqVO.setPassword("E10ADC3949BA59ABBE56E057F20F883E");
        orderApi.confirmOrder(1111, confirmOrderReqVO);
    }

}
