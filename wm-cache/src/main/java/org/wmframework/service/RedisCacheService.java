package org.wmframework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisCacheService implements RedisCacheManage {

    public static final long DEFAULT_EXPIRE = 60 * 60 * 24;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        log.info("add redis cache success,key:{};expire:{}", key, timeout);
    }

    @Override
    public void setDefaultExpire(String key, String value) {
        set(key, value, DEFAULT_EXPIRE, TimeUnit.SECONDS);
        log.info("add redis DEFAULT_EXPIRE cache success,key:{}", key);
    }

    @Override
    public void setRandomExpire(String key, String value) {
        Random random = new Random();
        int timeout = random.nextInt(Integer.parseInt(String.valueOf(DEFAULT_EXPIRE)));
        set(key, value, timeout, TimeUnit.SECONDS);
        log.info("add redis RANDOM_EXPIRE cache success,key:{}", key);
    }

    @Override
    public boolean updateExpire(String key, long timeout, TimeUnit timeUnit) {
        Boolean expireRes = stringRedisTemplate.expire(key, timeout, timeUnit);
        log.info("update cache expire success,key:{},expire:{}", key, timeout);
        return expireRes != null && expireRes;
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean delete(String key) {
        Boolean deleteRes = stringRedisTemplate.delete(key);
        return null != deleteRes && deleteRes;
    }

    @Override
    public void update(String key, String value, long timeout, TimeUnit timeUnit) {
        set(key, value, timeout, timeUnit);
    }

}
