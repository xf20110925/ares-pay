package com.ptb.pay.mapper.impl;

import com.ptb.pay.mapper.MyMapper;
import com.ptb.pay.model.ArticleChannel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ArticleChannelMapper extends MyMapper<ArticleChannel> {

    @Select("select * from article_channel where id=#{id}")
    @ResultMap("BaseResultMap")
    ArticleChannel selectInfoById(@Param("id") int id);

    @Select(value = "SELECT * from article_channel WHERE recommend_id = #{typeId} and max_num > 5")
    @ResultMap("BaseResultMap")
    List<ArticleChannel> selectRecommendArticleByTypeid(@Param("typeId") int typeId);

    @Select(value = "SELECT * from article_channel WHERE max_num > 5")
    @ResultMap("BaseResultMap")
    List<ArticleChannel> selectRecommendArticleByMaxNum();
}