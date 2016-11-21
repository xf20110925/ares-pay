package com.ptb.pay.api;

/**
 * Created by MyThinkpad on 2016/11/18.
 */

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.vo.product.ProductListVO;
import com.ptb.pay.vo.product.ProductVO;

/**
 * 商品相关操作
 */
public interface IProductApi {

    /**
     * 生成商品
     * @param productVO 商品对象
     * @return
     */
    public ResponseVo<ProductVO> generateProduct(ProductVO productVO);


    /**
     * 获取某一个用户的相应状态的商品列表
     * @param userId 用户ID
     * @param status 商品状态
     * @param start 列表开始索引
     * @param end  列表结束索引
     * @return
     */
    public ResponseVo<ProductListVO>  getUserProductList(long userId, int status, int start, int end);


    /**
     * 更新商品信息
     * @param userId 用户ID
     * @param productId 商品ID
     * @param desc  更新后的商品描述
     * @param prince 更新后的价格
     * @param state 更新后的状态
     * @return
     */
    public ResponseVo updateProductInfo(long userId, long productId, String desc, Long prince, Integer state);


    /**
     * 获取包含某一个具体物品的的商品列表
     * @param userId 用户ID
     * @param relevantId 物品ID
     * @param status 商品状态
     * @param start 列表开始索引
     * @param end 列表结束索引
     * @return
     */
    public ResponseVo<ProductListVO>  getProductList(long userId, long relevantId, int status, int start, int end);

    /**
     * 获取某一个商品的成交量
     * @param productId 商品ID
     * @return
     */
    public ResponseVo<Integer>  getProductDealNum(long productId);

    /**
     * 获取用户商品个数
     * @param userId 用户ID
     * @return
     */
    public ResponseVo<Integer> getUserProductNum(long userId);

    /**
     * 根据商品ID获取商品信息
     * @param userId
     * @param productId
     * @return
     */
    public ResponseVo<ProductVO> getProduct(long userId, long productId);
}
