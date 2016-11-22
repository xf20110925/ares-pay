package com.ptb.pay.api.impl;

import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.BaseTest;
import com.ptb.pay.api.IOrderApi;
import com.ptb.pay.enums.OrderStatusEnum;
import com.ptb.pay.enums.UserType;
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
        orderListReqVO.setUserId(10);
        orderListReqVO.setUserType(UserType.USER_IS_SELLER.getUserType());
        orderListReqVO.setDeviceTypeEnum(DeviceTypeEnum.android);
        orderListReqVO.setOrderStatus(OrderStatusEnum.ORDER_STATUS_NEW_DEAL.getStatus());
        orderApi.getOrderList(10, orderListReqVO);

        orderListReqVO.setOrderStatus(OrderStatusEnum.ORDER_STATUS_DEALING.getStatus());
        orderApi.getOrderList(10, orderListReqVO);

        orderListReqVO.setOrderStatus(OrderStatusEnum.ORDER_STATUS_DEAL_ALL.getStatus());
        orderApi.getOrderList(10, orderListReqVO);

    }

    @Test
    public void getMediaDetailInfo(){
        orderApi.getOrderInfo(10, 6L);
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
            ResponseVo responseVo = orderApi.cancelOrder(777l, 3);
            System.out.println(responseVo.getMessage());
        }catch (Exception e){

        }
    }
}
