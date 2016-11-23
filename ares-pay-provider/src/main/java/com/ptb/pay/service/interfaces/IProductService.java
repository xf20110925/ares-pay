package com.ptb.pay.service.interfaces;

import com.ptb.pay.model.Product;
import com.ptb.pay.vo.product.ProductVO;

/**
 * Created by watson zhang on 2016/11/19.
 */
public interface IProductService {

    public int insertProduct(ProductVO productVO);

    public ProductVO convertProductToVo(Product product);
}
