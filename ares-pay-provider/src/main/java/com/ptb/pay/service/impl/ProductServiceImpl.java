package com.ptb.pay.service.impl;

import com.ptb.pay.mapper.impl.ProductMapper;
import com.ptb.pay.model.Product;
import com.ptb.pay.service.interfaces.IProductService;
import com.ptb.pay.vo.product.ProductVO;
import com.ptb.pay.vopo.ConvertProductUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by watson zhang on 2016/11/19.
 */
@Service("productService")
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

        Product product = ConvertProductUtil.convertProductVOToProduct(productVO);
        int insert = productMapper.insert(product);
        if (insert < 1){
            logger.error("insert product error! productVO:{}", productVO);
            return -1;
        }
        return 0;
    }

    @Override
    public ProductVO convertProductToVo(Product product) {
        if ( null == product){
            return null;
        }
        return ConvertProductUtil.convertProductToProductVO(product);
    }
}
