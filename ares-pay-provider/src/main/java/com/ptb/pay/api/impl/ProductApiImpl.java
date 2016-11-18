package com.ptb.pay.api.impl;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IProductApi;
import com.ptb.pay.vo.product.ProductListVO;
import com.ptb.pay.vo.product.ProductVO;
import org.springframework.stereotype.Component;

/**
 * Created by MyThinkpad on 2016/11/18.
 */
@Component("productApi")
public class ProductApiImpl implements IProductApi {

    @Override
    public ResponseVo<ProductVO> generateProduct(ProductVO productVO) {
        return null;
    }

    @Override
    public ResponseVo<ProductListVO> getUserProductList(long userId, int status, int start, int end) {
        return null;
    }

    @Override
    public ResponseVo updateProductInfo(long userId, long productId, String desc, Long prince, Integer state) {
        return null;
    }

    @Override
    public ResponseVo<ProductListVO> getProductList(long userId, long relevantId, int status, int start, int end) {
        return null;
    }

    @Override
    public ResponseVo<Integer> getProductDealNum(long productId) {
        return null;
    }

}
