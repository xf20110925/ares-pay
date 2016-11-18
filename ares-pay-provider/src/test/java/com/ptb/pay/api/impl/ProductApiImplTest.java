package com.ptb.pay.api.impl;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.BaseTest;
import com.ptb.pay.api.IProductApi;
import com.ptb.pay.vo.product.ProductOwnerType;
import com.ptb.pay.vo.product.ProductType;
import com.ptb.pay.vo.product.ProductVO;
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
        ProductVO productVO = new ProductVO(111, ProductOwnerType.USER_TYPE.getType(), "test1", ProductType.MEDIA_SERVICE_TYPE.getType(), 123);
        ResponseVo responseVo = productApi.generateProduct(productVO);
        Assert.assertTrue(responseVo.getCode().equals("0"));
    }

    @Test
    public void getUserProductListTest(){

    }

    @Test
    public void getProductListTest(){

    }

    @Test
    public void updateProductInfoTest(){

    }

    @Test
    public void getProductDealNumTest(){

    }
}
