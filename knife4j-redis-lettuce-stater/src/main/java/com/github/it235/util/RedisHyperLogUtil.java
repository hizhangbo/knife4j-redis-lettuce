package com.github.it235.util;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

@ConditionalOnBean(RedisBaseUtil.class)
public class RedisHyperLogUtil extends RedisBaseUtil {
    public Long add(int dbIndex, String key, Object... values) {
        return knife4jRedisManager.redisTemplate(dbIndex).opsForHyperLogLog().add(key, values);
    }

    /**
     * Gets the current number of elements within the {@literal key}.
     *
     * @param keys must not be {@literal null} or {@literal empty}.
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Long size(int dbIndex, String... keys) {
        return knife4jRedisManager.redisTemplate(dbIndex).opsForHyperLogLog().size(keys);
    }

    /**
     * Merges all values of given {@literal sourceKeys} into {@literal destination} key.
     *
     * @param destination key of HyperLogLog to move source keys into.
     * @param sourceKeys  must not be {@literal null} or {@literal empty}.
     * @return {@literal null} when used in pipeline / transaction.
     */
    public Long union(int dbIndex, String destination, String... sourceKeys) {
        return knife4jRedisManager.redisTemplate(dbIndex).opsForHyperLogLog().union(destination, sourceKeys);
    }

    /**
     * Removes the given {@literal key}.
     *
     * @param key must not be {@literal null}.
     */
    public void delete(int dbIndex, String key) {
        knife4jRedisManager.redisTemplate(dbIndex).opsForHyperLogLog().delete(key);
    }
}
