package com.ptb.pay.service.impl;

import com.ptb.pay.mapper.impl.ProductMapper;
import com.ptb.pay.model.Product;
import com.ptb.pay.service.interfaces.IProductService;
import com.ptb.pay.vo.product.ProductVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by watson zhang on 2016/11/19.
 */
public class ProductServiceImpl implements IProductService{
    private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    ProductMapper productMapper;

    @Override
    public int insertProduct(ProductVO productVO) {

        if (productVO == null){
            logger.error("insert product error! productVO is null");
            return -1;
        }

        Product product = new Product();
        product.setProductName(productVO.getProductName());
        product.setProductType(productVO.getProductType());
        product.setPrice(productVO.getPrice());
        product.setCreateTime(new Date(productVO.getCreateTime()));
        product.setLastUpdateTime(new Date(productVO.getUpdateTime()));
        product.setOwnerId(productVO.getOwnerId());
        product.setOwnerType(productVO.getOwnerType());
        product.setStatus(productVO.getStatus());
        product.setDesc(productVO.getDesc());
        product.setDealNum(productVO.getDealNum());
        product.setRelevantId(productVO.getRelevantId());

        int insert = productMapper.insert(product);
        if (insert < 1){
            logger.error("insert product error! productVO:{}", productVO);
            return -1;
        }
        return 0;
    }
}
