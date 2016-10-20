package com.ptb.pay.util.redis;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zuokui.fu on 2016/10/19.
 */
public class JedisPoolManager {

    private static Logger logger = LoggerFactory.getLogger( JedisPoolManager.class);

    private ShardedJedisPool shardPool;

    class JedisConfig {
        private JedisConfig() {
            initaize();
        }

        private void initaize() {
            logger.info("redis initaizing ...");
            Configuration config = null;
            try {
                config = new PropertiesConfiguration( "redis.properties");
            } catch (ConfigurationException e) {
                throw new RuntimeException( "redis.properties not found", e);
            }
            JedisPoolConfig pConfig = new JedisPoolConfig();
            pConfig.setMaxTotal(config.getInt("redis.maxTotle"));
            pConfig.setMaxIdle(config.getInt("redis.maxIdle"));
            pConfig.setMinIdle(config.getInt("redis.minIdle"));

            // 单位:毫秒
            Integer poolTimeout = config.getInt("redis.pool.timeout");
            String hostInfo = config.getString( "redis.cluster.host.info", "127.0.0.1:6379:123456");
            String[] hostInfoArray = hostInfo.split( ";");
            List<JedisShardInfo> jdinfoList = new ArrayList<JedisShardInfo>();
            for ( String host : hostInfoArray) {
                String[] singleHost = host.split( ":");
                String masterHost = singleHost[0];
                String masterPort = singleHost[1];
                JedisShardInfo jsi = new JedisShardInfo(masterHost, Integer.valueOf(masterPort), poolTimeout);
                jdinfoList.add(jsi);
            }

            shardPool = new ShardedJedisPool(pConfig, jdinfoList);
            logger.info("redis initialize success");
        }
    }

    private JedisPoolManager() {
        new JedisConfig();
    }

    private static JedisPoolManager instance = new JedisPoolManager();

    public static JedisPoolManager getInstance() {
        return instance;
    }

    public ShardedJedis getShardedJedis() {
        return shardPool.getResource();
    }

    /**
     * 正常归还jedis
     *
     * @param jedis
     */
    public void returnSharedJedis(final ShardedJedis jedis) {
        shardPool.returnResource(jedis);
    }

    /**
     * jedis出现异常的时候销毁jedis
     *
     * @param jedis
     */
    public void returnBrokenSharedJedis(final ShardedJedis jedis) {
        shardPool.returnBrokenResource(jedis);
    }

}

