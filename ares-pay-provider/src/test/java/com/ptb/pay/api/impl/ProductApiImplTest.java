package com.ptb.pay.api.impl;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.BaseTest;
import com.ptb.pay.api.IProductApi;
import com.ptb.pay.vo.product.*;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by MyThinkpad on 2016/11/18.
 */
public class ProductApiImplTest extends BaseTest {

    @Autowired
    private IProductApi productApi;

    @Test
    public void generateProductTest(){
        ProductVO productVO = new ProductVO(1, ProductOwnerType.USER_TYPE.getType(), "test3", ProductType.MEDIA_SERVICE_TYPE.getType(), 1);
        productVO.setPmid("aaaaaaaaaaaaaaaa");
        productVO.setMediaType(123);
        ResponseVo responseVo = productApi.generateProduct(productVO);
        Assert.assertTrue(responseVo.getCode().equals("0"));
    }

    @Test
    public void getUserProductListTest(){
        ResponseVo<ProductListVO> responseVo = productApi.getUserProductList(111, ProductState.PRODUCT_ON_OFF_SELL.getStatus(), 0, 10);
        Assert.assertTrue(responseVo.getCode().equals("0"));
    }

    @Test
    public void getProductListTest(){
        ResponseVo<ProductListVO> responseVo = productApi.getProductList(111, 124, ProductState.PRODUCT_ON_SELL.getStatus(), 0, 10);
        Assert.assertTrue(responseVo.getCode().equals("0"));
    }

    @Test
    public void updateProductInfoTest(){
        ResponseVo responseVo = productApi.updateProductInfo(1, 79, null, 22220000L, 1);
        Assert.assertTrue(responseVo.getCode().equals("0"));
    }

    @Test
    public void getProductDealNumTest(){
        ResponseVo<Integer> responseVo = productApi.getProductDealNum(124);
        Assert.assertTrue(responseVo.getCode().equals("0"));
    }

    @Test
    public void getUserProductNum(){
        ResponseVo<Integer> responseVo = productApi.getUserProductNum(111);
        Assert.assertTrue(responseVo.getCode().equals("0"));
    }
}
