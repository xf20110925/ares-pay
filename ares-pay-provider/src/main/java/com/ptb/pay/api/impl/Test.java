package com.ptb.pay.api.impl;

import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.ITestApi;
import com.ptb.pay.mapper.impl.ArticleChannelMapper;
import com.ptb.pay.model.ArticleChannel;
import com.ptb.pay.util.redis.JedisUtil;
import com.ptb.utils.service.ReturnUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zuokui.fu on 2016/10/18.
 */
@Component("testApi")
public class Test implements ITestApi {

    private static Logger logger = LoggerFactory.getLogger( Test.class);

    @Autowired
    private ArticleChannelMapper articleChannelMapper;

    public ResponseVo<Object> test(){
        List<ArticleChannel> list = articleChannelMapper.selectAll();
        logger.info( "===================={}", list.size());
        for ( ArticleChannel articleChannel : list){
            logger.info( "name:{}", articleChannel.getName());
        }
        ArticleChannel articleChannel = articleChannelMapper.selectInfoById( 11);
        logger.error( "*******************name:{}", articleChannel.getName());

        return ReturnUtil.success();
    }
}
