package com.ptb.pay.vopo;

import com.ptb.pay.model.Product;
import com.ptb.pay.vo.product.ProductVO;

import java.util.Date;

/**
 * Created by MyThinkpad on 2016/11/18.
 */
public class ConvertProductUtil {
    public static Product convertProductVOToProduct(ProductVO productVO){
        Product product = new Product();
        product.setCreateTime(new Date(productVO.getCreateTime()));
        product.setLastUpdateTime(new Date(productVO.getUpdateTime()));
        product.setDealNum(productVO.getDealNum());
        product.setDesc(productVO.getDesc());
        product.setOwnerId(productVO.getOwnerId());
        product.setOwnerType(productVO.getOwnerType());
        product.setPrice(productVO.getPrice());
        product.setProductName(productVO.getProductName());
        product.setProductType(productVO.getProductType());
        product.setRelevantId(productVO.getRelevantId());
        product.setStatus(productVO.getStatus());
        return product;
    }
    public static ProductVO convertProductToProductVO(Product product){
        ProductVO productVO = new ProductVO();
        productVO.setCreateTime(product.getCreateTime().getTime());
        productVO.setUpdateTime(product.getLastUpdateTime().getTime());
        productVO.setDealNum(product.getDealNum());
        productVO.setDesc(product.getDesc());
        productVO.setOwnerId(product.getOwnerId());
        productVO.setOwnerType(product.getOwnerType());
        productVO.setPrice(product.getPrice());
        productVO.setProductName(product.getProductName());
        productVO.setProductType(product.getProductType());
        productVO.setRelevantId(product.getRelevantId());
        productVO.setStatus(product.getStatus());
        productVO.setProductId(product.getPtbProductId());
        return productVO;
    }
}
