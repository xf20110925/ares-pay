package com.ptb.pay.api.impl;

import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.PlatformEnum;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.BaseTest;
import com.ptb.pay.api.IOrderApi;
import com.ptb.pay.enums.OrderStatusEnum;
import com.ptb.pay.enums.UserTypeEnum;
import com.ptb.pay.vo.order.ConfirmOrderReqVO;
import com.ptb.pay.vo.order.OrderListReqVO;
import com.ptb.pay.vo.order.OrderQueryVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

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
    public void getOrderListDynamics() {
        OrderQueryVO orderQueryVO = new OrderQueryVO();
        orderQueryVO.setOrderNo("JYPP161206190055000021");
        orderQueryVO.setOrderStatus(OrderStatusEnum.ORDER_STATUS_NEW_DEAL.getStatus());
        //orderQueryVO.setStartTime(new Date(new Date().getTime() - 5 * 24 * 60 * 60 * 1000));
        //orderQueryVO.setEndTime(new Date(new Date().getTime() - 3 * 24 * 60 * 60 * 1000));
        //orderQueryVO.setOrderStatus(OrderStatusEnum.ORDER_STATUS_DEALING.getStatus());
        orderApi.getOrderListByPage(1, 10, orderQueryVO);
    }

    @Test
    public void getOrderInfo(){
        ResponseVo orderInfo = orderApi.getOrderInfo(2, 332l);
        System.out.println(orderInfo.getCode());
    }

    @Test
    public void getOrderList(){
        //所有订单
        OrderListReqVO orderListReqVO = new OrderListReqVO();
        orderListReqVO.setStart(0);
        orderListReqVO.setEnd(10);
        orderListReqVO.setUserId(776);
        orderListReqVO.setUserType(UserTypeEnum.USER_IS_SELLER.getUserType());
        orderListReqVO.setDeviceTypeEnum(DeviceTypeEnum.android);
        orderListReqVO.setOrderStatus(OrderStatusEnum.ORDER_STATUS_DEAL_ALL.getStatus());
        orderApi.getOrderList(776, orderListReqVO);

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
            orderApi.submitOrder(112, 41, "test", "iphone");
        }catch (Exception e){

        }
    }

    @Test
    public void cancelOrderTest(){
        try {
            ResponseVo responseVo = orderApi.cancelOrder(767, 166);
            System.out.println(responseVo.getMessage());
        }catch (Exception e){

        }
    }

    @Test
    public void sellerConfirmOrder(){
        ConfirmOrderReqVO confirmOrderReqVO = new ConfirmOrderReqVO();
        confirmOrderReqVO.setOrderId(5);
        confirmOrderReqVO.setUserId(1111);
        confirmOrderReqVO.setUserType(UserTypeEnum.USER_IS_SELLER.getUserType());
        confirmOrderReqVO.setDeviceTypeEnum(DeviceTypeEnum.android);
        confirmOrderReqVO.setPassword("123123123");
        orderApi.confirmOrder(1111, confirmOrderReqVO);
    }

    @Test
    public void buyerConfirmOrder(){
        ConfirmOrderReqVO confirmOrderReqVO = new ConfirmOrderReqVO();
        confirmOrderReqVO.setOrderId(5);
        confirmOrderReqVO.setUserId(1111);
        confirmOrderReqVO.setUserType(UserTypeEnum.USER_IS_BUYER.getUserType());
        confirmOrderReqVO.setDeviceTypeEnum(DeviceTypeEnum.android);
        confirmOrderReqVO.setPlatformEnum(PlatformEnum.xiaomi);
        confirmOrderReqVO.setPassword("E10ADC3949BA59ABBE56E057F20F883E");
        orderApi.confirmOrder(1111, confirmOrderReqVO);
    }

    @Test
    public void getOrderLogByOrderNo(){
        //ResponseVo orderLogByOrderNo = orderApi.getOrderLogForPageByOrderNo(2, 5,"JYAP161124155041000000");
        ResponseVo orderLogByOrderNo = orderApi.getOrderLogByOrderNo("JYAP161124155041000000");

        System.out.println(orderLogByOrderNo.getData());

    }

    @Test
    public void forceCompleteByAdmin(){
        ResponseVo responseVo = orderApi.forceCompleteByAdmin(2, 20, "test aaaaaaaaaaaaaa");
        System.out.println(responseVo.getCode());
    }

    @Test
    public void forceRefundByAdmin(){
        ResponseVo responseVo = orderApi.forceRefundByAdmin(2, 68, "test abbgagag");

        System.out.println(responseVo.getCode());
    }

}
