package com.ptb.pay.api.impl;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IProductApi;
import com.ptb.pay.enums.ErrorCode;
import com.ptb.pay.mapper.impl.ProductMapper;
import com.ptb.pay.model.Product;
import com.ptb.pay.service.interfaces.IProductService;
import com.ptb.pay.vo.product.ProductListVO;
import com.ptb.pay.vo.product.ProductState;
import com.ptb.pay.vo.product.ProductVO;
import com.ptb.pay.vopo.ConvertProductUtil;
import com.ptb.utils.service.ReturnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by MyThinkpad on 2016/11/18.
 */
@Component("productApi")
public class ProductApiImpl implements IProductApi {
    private static Logger logger = LoggerFactory.getLogger(ProductApiImpl.class);

    @Autowired
    ProductMapper productMapper;
    @Autowired
    private IProductService productService;

    @SuppressWarnings("unchecked")
    @Override
    public ResponseVo<ProductVO> generateProduct(ProductVO productVO) {
        //查询校参
        if(null == productVO || null == productVO.getOwnerId() || null == productVO.getRelevantId()) {
            return ReturnUtil.error(ErrorCode.PRODUCT_API_PARAMETER_ERROR.getCode(), ErrorCode.PRODUCT_API_PARAMETER_ERROR.getMessage());
        }

        //查看是否存在该媒体
        Product product = productMapper.selectByUidAndRelevantId(productVO.getOwnerId(), productVO.getRelevantId());
        if(null == product){
            try {
                product = ConvertProductUtil.convertProductVOToProduct(productVO);
                productMapper.insertReturnId(product);
                return ReturnUtil.success(ConvertProductUtil.convertProductToProductVO(product));
            }catch (DuplicateKeyException ignored){
            }catch (Exception ee){
                ee.printStackTrace();
                return ReturnUtil.error(String.valueOf(ErrorCode.PAY_API_COMMMON_1000.getCode()), ErrorCode.PAY_API_COMMMON_1000.getMessage());
            }
        }
        return ReturnUtil.error(ErrorCode.PRODUCT_API_REPEAT.getCode(), ErrorCode.PRODUCT_API_REPEAT.getMessage());

    }

    @SuppressWarnings("unchecked")
    @Override
    public ResponseVo<ProductListVO> getUserProductList(long userId, int status, int start, int end) {
        ProductListVO productListVO = null;
        //查询用户所有订单列表
        List<Product> productList = productMapper.selectByUidOrderByCreateTime(userId);
        if(CollectionUtils.isEmpty(productList))
            return ReturnUtil.success(ProductListVO.Empty());

        //获取符合状态的商品
        if(status == ProductState.PRODUCT_OFF_SELL.getStatus() || status == ProductState.PRODUCT_ON_SELL.getStatus()){
            productList = productList.stream().filter(item->{return item.getStatus().equals(status);}).collect(Collectors.toList());
        }else if(status == ProductState.PRODUCT_ON_OFF_SELL.getStatus()){
            productList = productList.stream().filter(item->{return item.getStatus().equals(ProductState.PRODUCT_OFF_SELL.getStatus())||item.getStatus().equals(ProductState.PRODUCT_ON_SELL.getStatus());}).collect(Collectors.toList());
        }else{
            return ReturnUtil.error(ErrorCode.PRODUCT_API_PARAMETER_ERROR.getCode(), ErrorCode.PRODUCT_API_PARAMETER_ERROR.getMessage());
        }

        //分页
        productListVO = new ProductListVO(productList.size(),productList.stream().skip(start).limit(end-start).map(ConvertProductUtil::convertProductToProductVO).collect(Collectors.toList()));

        return ReturnUtil.success(productListVO);
    }

    @Override
    public ResponseVo updateProductInfo(long userId, long productId, String desc, Long prince, Integer state) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if(null == product || !product.getOwnerId().equals(userId)){
            return ReturnUtil.error(ErrorCode.PRODUCT_API_NO_EXISTS.getCode(), ErrorCode.PRODUCT_API_NO_EXISTS.getMessage());
        }

        if(state == ProductState.PRODUCT_ON_SELL.getStatus() && ( prince == null || prince < 1) && product.getPrice() < 1)
            return ReturnUtil.error(ErrorCode.PRODUCT_API_PRICE_LESS_ONE.getCode(), ErrorCode.PRODUCT_API_PRICE_LESS_ONE.getMessage());

        product.setDesc(desc);
        product.setPrice(prince);
        product.setStatus(state);
        product.setLastUpdateTime(new Date());
        if(productMapper.updateByPrimaryKeySelective(product) < 1)
            return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1000.getCode(), ErrorCode.PAY_API_COMMMON_1000.getMessage());

        return ReturnUtil.success();
    }

    @Override
    public ResponseVo updateProductDealNum(long userId, long productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if(null == product){
            return ReturnUtil.error(ErrorCode.PRODUCT_API_NO_EXISTS.getCode(), ErrorCode.PRODUCT_API_NO_EXISTS.getMessage());
        }

        if(productMapper.updateDealNumByProductId(productId, new Date(), product.getDealNum()) < 1)
            return ReturnUtil.error(ErrorCode.PAY_API_COMMMON_1000.getCode(), ErrorCode.PAY_API_COMMMON_1000.getMessage());

        return ReturnUtil.success();
    }


    @SuppressWarnings("unchecked")
    @Override
    public ResponseVo<ProductListVO> getProductList(long userId, long relevantId, int status, int start, int end) {
        ProductListVO productListVO = null;

        //获取包含指定物品的商品列表
        List<Product> productList = productMapper.selectByRelevantId(relevantId);
        if(CollectionUtils.isEmpty(productList))
            return ReturnUtil.success(ProductListVO.Empty());

        //获取符合状态的商品
        if(status == ProductState.PRODUCT_OFF_SELL.getStatus() || status == ProductState.PRODUCT_ON_SELL.getStatus()){
            productList = productList.stream().filter(item->{return item.getStatus().equals(status);}).collect(Collectors.toList());
        }else if(status == ProductState.PRODUCT_ON_OFF_SELL.getStatus()){
            productList = productList.stream().filter(item->{return item.getStatus().equals(ProductState.PRODUCT_OFF_SELL.getStatus())||item.getStatus().equals(ProductState.PRODUCT_ON_SELL.getStatus());}).collect(Collectors.toList());
        }else{
            return ReturnUtil.error(ErrorCode.PRODUCT_API_PARAMETER_ERROR.getCode(), ErrorCode.PRODUCT_API_PARAMETER_ERROR.getMessage());
        }

        //分页
        productListVO = new ProductListVO(productList.size(),productList.stream().skip(start).limit(end-start).map(ConvertProductUtil::convertProductToProductVO).collect(Collectors.toList()));

        return ReturnUtil.success(productListVO);

    }

    @SuppressWarnings("unchecked")
    @Override
    public ResponseVo<Integer> getProductDealNum(long relevantId) {
        List<Product> products = productMapper.selectByRelevantId(relevantId);
        if(CollectionUtils.isEmpty(products))
            return ReturnUtil.error(ErrorCode.PRODUCT_API_NO_EXISTS.getCode(), ErrorCode.PRODUCT_API_NO_EXISTS.getMessage());
        final int[] num = {0};
        products.forEach(item->{
            num[0] +=item.getDealNum();
        });
        return ReturnUtil.success(num[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResponseVo<Integer> getUserProductNum(long userId){
        int num = productMapper.selectProductNumByUid(userId);
        return ReturnUtil.success(num);
    }

    @Override
    public ResponseVo<ProductVO> getProduct(long userId, long productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            logger.error("get product error! userId:{} productId:{}", userId, productId);
            return ReturnUtil.error("30001","get product error!");
        }
        return new ResponseVo<>("0","get product success",ConvertProductUtil.convertProductToProductVO(product));
    }

    @Override
    public ResponseVo<ProductVO> getProduct(String orderNo) {
        Product product = productMapper.getProductByOrderNo( orderNo);
        if (product == null){
            logger.error("get product error! orderNo:{}", orderNo);
            return ReturnUtil.error("30001","get product error!");
        }
        return ReturnUtil.success( productService.convertProductToVo( product));
    }

    @Override
    public ResponseVo<Map<String, Object>> getProductNameByOrdreNos(List<String> orderNos) {
        List<Map<String, Object>> list = productMapper.getProductNameByOrderNos( orderNos);
        Map<String, Object> result = new HashMap<>();
        for ( Map<String, Object> product : list){
            result.put( String.valueOf(product.get("order_no")), product.get("product_name"));
        }
        return ReturnUtil.success( result);
    }

}
