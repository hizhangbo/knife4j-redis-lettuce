package com.github.it235.util;


import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description: 单值/对象操作
 * @author: jianjun.ren
 * @date: Created in 2020/9/27 11:45
 */
@ConditionalOnBean(RedisBaseUtil.class)
public class RedisValUtil extends RedisBaseUtil {

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        return set(defaultDB, key, value);
    }

    public boolean set(int dbIndex, String key, Object value) {
        try {
            knife4jRedisManager.redisTemplate(dbIndex).opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取 String 类型 key-value
     *
     * @param key 键
     * @return
     */
    public String get(String key) {
        return get(defaultDB, key);
    }

    public String get(int dbIndex, String key) {
        Object result = knife4jRedisManager.redisTemplate(dbIndex).opsForValue().get(key);
        return result == null ? null : (String) result;
    }

    /**
     * 获取单对象 redis<String,Clazz>
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        return get(defaultDB, key, clazz);
    }

    public <T> T get(int dbIndex, String key, Class<T> clazz) {
        Object o = knife4jRedisManager.redisTemplate(dbIndex).opsForValue().get(key);
        if (o instanceof JSONObject) {
            JSONObject jo = (JSONObject) o;
            T t = jo.toJavaObject(clazz);
            return t;
        }
        T t = JSONObject.parseObject(JSONObject.toJSONString(o), clazz);
        return t;
    }

    /**
     * 带失效时间设置
     *
     * @param key     键
     * @param value   值
     * @param timeout 失效时间（秒）
     */
    public void set(String key, Object value, long timeout) {
        set(defaultDB, key, value, timeout, TimeUnit.SECONDS);
    }

    public void set(int dbIndex, String key, Object value, long timeout, TimeUnit timeUnit) {
        knife4jRedisManager.redisTemplate(dbIndex).opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 为多个键分别设置他们的值
     * 使用：Map<String,String> maps = new HashMap<String, String>();
     * maps.put("multi1","ccc");
     * maps.put("multi2","xxx");
     * maps.put("multi3","zzz");
     * template.opsForValue().multiSet(maps);
     *
     * @param map
     */
    protected void multiSetObj(Map<String, Object> map) {
        multiSetObj(defaultDB, map);
    }

    protected void multiSetObj(int dbIndex, Map<String, Object> map) {
        knife4jRedisManager.redisTemplate(dbIndex).opsForValue().multiSet(map);
    }

    /***
     * 分多个键同时取出他们的值
     * List<String> keys = new ArrayList<String>();
     keys.add("multi1");
     keys.add("multi2");
     keys.add("multi3");
     System.out.println(template.opsForValue().multiGet(keys));
     结果：[ccc, xxx, zzz]
     * @param keys
     *
     */
    protected List<Object> multiGetObj(List<String> keys) {
        return multiGetObj(defaultDB, keys);
    }

    protected List<Object> multiGetObj(int dbIndex, List<String> keys) {
        return knife4jRedisManager.redisTemplate(dbIndex).opsForValue().multiGet(keys);
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        return incr(defaultDB, key, delta);
    }

    public long incr(int dbIndex, String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return knife4jRedisManager.redisTemplate(dbIndex).opsForValue().increment(key, delta);
    }

    public long incrFiled(String key, String filed) {
        return incrFiled(defaultDB, key, filed);
    }

    public long incrFiled(int dbIndex, String key, String filed) {
        return knife4jRedisManager.redisTemplate(dbIndex).opsForHash().increment(key, filed, 1);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        return decr(defaultDB, key, delta);
    }

    public long decr(int dbIndex, String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return knife4jRedisManager.redisTemplate(dbIndex).opsForValue().increment(key, -delta);
    }

    //存hash
    public void setHash(String key, String filed, String value) {
        setHash(defaultDB, key, filed, value);
    }

    public void setHash(int dbIndex, String key, String filed, String value) {
        knife4jRedisManager.redisTemplate(dbIndex).opsForHash().put(key, filed, value);
    }

    //取hash
    public String getHash(String key, String filed) {
        return getHash(defaultDB, key, filed);
    }

    public String getHash(int dbIndex, String key, String filed) {
        Object o = knife4jRedisManager.redisTemplate(dbIndex).opsForHash().get(key, filed);
        return (String) o;
    }

    public void setList(String key, List<String> list) {
        setList(defaultDB, key, list);
    }

    public void setList(int dbIndex, String key, List<String> list) {
        knife4jRedisManager.redisTemplate(dbIndex).opsForList().leftPush(key, list);
    }

    public List<String> getList(String key) {
        return getList(defaultDB, key);
    }

    public List<String> getList(int dbIndex, String key) {
        return (List<String>) knife4jRedisManager.redisTemplate(dbIndex).opsForList().leftPop(key);
    }

    public void lPush(String key, String value) {
        lPush(defaultDB, key, value);
    }

    public void lPush(int dbIndex, String key, String value) {
        knife4jRedisManager.redisTemplate(dbIndex).opsForList().leftPush(key, value);
    }

    public String lPop(String key) {
        return lPop(defaultDB, key);
    }

    public String lPop(int dbIndex, String key) {
        return (String) knife4jRedisManager.redisTemplate(dbIndex).opsForList().leftPop(key);
    }

    public Long listSize(String key) {
        return listSize(defaultDB, key);
    }

    public Long listSize(int dbIndex, String key) {
        return knife4jRedisManager.redisTemplate(dbIndex).opsForList().size(key);
    }


    //查询list中指定范围的内容
    public List<String> range(String key) {
        return range(defaultDB, key);
    }

    public List<String> range(int dbIndex, String key) {
        List<Object> range = knife4jRedisManager.redisTemplate(dbIndex).opsForList().range(key, 0, -1);
        List<String> result = new ArrayList<>();
        for (Object o : range) {
            result.add((String) o);
        }
        return result;
    }

    public Long ttl(String key) {
        return ttl(defaultDB, key);
    }

    public Long ttl(int dbIndex, String key) {
        return knife4jRedisManager.redisTemplate(dbIndex).opsForValue().getOperations().getExpire(key);
    }

    public Set<String> getKeys(String redisKey) {
        return getKeys(0, redisKey);
    }

    public Set<String> getKeys(int dbIndex, String redisKey) {
        Set<Object> keys = knife4jRedisManager.redisTemplate(dbIndex).opsForValue().getOperations().keys(redisKey);
        Set<String> retKeys = new HashSet<>();
        for (Object key : keys) {
            retKeys.add(String.valueOf(key));
        }
        return retKeys;
    }

    public Boolean getBit(int dbIndex, String redisKey, long offset) {
        return knife4jRedisManager.redisTemplate(dbIndex).opsForValue().getBit(redisKey, offset);
    }

    public Boolean setBit(int dbIndex, String redisKey, long offset, boolean value) {
        return knife4jRedisManager.redisTemplate(dbIndex).opsForValue().setBit(redisKey, offset, value);
    }
}