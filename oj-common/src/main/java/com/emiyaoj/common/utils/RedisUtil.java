package com.emiyaoj.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public boolean hasKey(String s) {
        return redisTemplate.hasKey(s);
    }

    public void set(String tokenKey, String token, long l) {
        this.setWithExpire(tokenKey, token, l, TimeUnit.MILLISECONDS);
    }


    // 设置键值
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 获取键值
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 设置键值并设置过期时间
    public void setWithExpire(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // 删除键
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
