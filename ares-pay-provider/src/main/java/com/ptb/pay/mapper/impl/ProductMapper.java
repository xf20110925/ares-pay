package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

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

    @Select("select * from ptb_product where owner_id=#{uid}")
    int selectNumByUid(@Param("uid") long userId);

    @Select("select * from ptb_product where owner_id = #{ownerId}")
    public List<Product> selectByOwnerId(@Param("ownerId") long ownerId);
}