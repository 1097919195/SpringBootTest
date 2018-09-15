/** */
package com.example.springboottest.utils.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Component
public class JedisUtil {
    @Autowired
    private JedisPool jedisPool;

    private Jedis jedis;

    public JedisUtil() {
    }

    public Jedis getJedis() {
        if (jedis == null) {
            jedis = jedisPool.getResource();
        }
        return jedis;
    }

    /**
     * 根据key查看是否存在
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return getJedis().exists(key);
    }

    /**
     * 设置key -value 形式数据
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        String result = getJedis().set(key, value);
        return result;
    }

    /**
     * 设置 一个过期时间
     *
     * @param key
     * @param value
     * @param timeOut 单位秒
     * @return
     */
    public String set(String key, String value, int timeOut) {
        return getJedis().setex(key, timeOut, value);
    }

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */
    public String getByKey(String key) {
        return getJedis().get(key);
    }

    /**
     * 根据通配符获取所有匹配的key
     *
     * @param pattern
     * @return
     */
    public Set<String> getKesByPattern(String pattern) {
        return getJedis().keys(pattern);
    }

    /**
     * 根据key删除
     *
     * @param key
     */
    public void delByKey(String key) {
        getJedis().del(key);
    }

    /**
     * 根据key获取过期时间
     *
     * @param key
     * @return
     */
    public long getTimeOutByKey(String key) {
        return getJedis().ttl(key);
    }

    /**
     * 清空数据 【慎用啊！】
     */
    public void flushDB() {
        getJedis().flushDB();
    }

    /**
     * 刷新过期时间
     *
     * @param key
     * @param timeOut
     * @return
     */
    public long refreshLiveTime(String key, int timeOut) {
        return getJedis().expire(key, timeOut);
    }
}