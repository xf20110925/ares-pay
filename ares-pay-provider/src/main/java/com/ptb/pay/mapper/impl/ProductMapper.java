package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductMapper extends MyMapper<Product>{

    @Select("select * from ptb_product where owner_id = #{ownerId}")
    public List<Product> selectByOwnerId(@Param("ownerId") long ownerId);


}