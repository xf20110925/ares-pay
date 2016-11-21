package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface ProductMapper extends MyMapper<Product>{
    int insertReturnId(Product record);

    @Select("select * from ptb_product where relevant_id=#{rUid} and owner_id=#{uid}")
    @ResultMap("BaseResultMap")
    Product selectByUidAndRelevantId(@Param("uid") Long ownerId, @Param("rUid") Long relevantId);

    @Select("select * from ptb_product where owner_id=#{uid} order by create_time desc")
    @ResultMap("BaseResultMap")
    List<Product> selectByUidOrderByCreateTime(@Param("uid") long userId);

    @Select("select * from ptb_product where relevant_id=#{rid} order by create_time desc")
    @ResultMap("BaseResultMap")
    List<Product> selectByRelevantId(@Param("rid") long relevantId);

    @Select("select count(*) from ptb_product where owner_id=#{uid}")
    int selectProductNumByUid(@Param("uid") long userId);

    @Select("select * from ptb_product where ptb_product_id = #{productId}")
    public List<Product> selectByProductId(@Param("productId") long productId);

    @Update("update ptb_product set deal_num=deal_num+1, last_update_time=#{dd} where ptb_product_id=#{pPid} and deal_num=#{dealNum}")
    int updateDealNumByProductId(@Param("pPid") long productId, @Param("dd") Date date, @Param("dealNum") Integer dealNum);
}