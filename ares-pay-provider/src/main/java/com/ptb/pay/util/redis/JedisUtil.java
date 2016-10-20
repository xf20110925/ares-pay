package com.ptb.pay.util.redis;

import redis.clients.jedis.ShardedJedis;

/**
 * Created by zuokui.fu on 2016/10/19.
 */
public class JedisUtil {

    private static JedisPoolManager pool = JedisPoolManager.getInstance();

    public static String get(String key) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.get(key);
        }
    }

    public static String set(String key, String value) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.set(key, value);
        }
    }

    public static Boolean exists(String key) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.exists(key);
        }
    }

    public static Long expire(String key, int seconds) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.expire(key, seconds);
        }
    }

    public static Long ttl(String key) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.ttl(key);
        }
    }

    public static Long incrBy(String key, long integer) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.incrBy(key, integer);
        }
    }

    public static Long decrBy(String key, long integer) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.decrBy(key, integer);
        }
    }

    public static Long decr(String key) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.decr(key);
        }
    }

    public static Long incr(String key) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.incr(key);
        }
    }

    public static String hget(String key, String field) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.hget(key, field);
        }
    }

    public static Long hset(String key, String field, String value) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.hset(key, field, value);
        }
    }

    public static Long del(String key) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.del(key);
        }
    }

    public static Long hdel(String key, String... fields) {
        try (ShardedJedis jedis = pool.getShardedJedis()) {
            return jedis.hdel(key, fields);
        }
    }
}
