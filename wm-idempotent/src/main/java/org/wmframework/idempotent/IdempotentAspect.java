package org.wmframework.idempotent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.wmframework.exception.BizException;
import org.wmframework.idempotent.annotations.Idempotent;
import org.wmframework.idempotent.common.IdempotentStrategy;
import org.wmframework.idempotent.context.IdempotentStrategyContext;
import org.wmframework.result.Resp;

import java.util.Optional;

/**
 * 接口幂等切面
 *
 * @author: 王锰
 * @date: 2019/8/18
 */
@Aspect
@Component
@Slf4j
public class IdempotentAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IdempotentStrategyContext idempotentStrategyContext;

    /**
     * 切点，标注了@Idempotent的controller方法
     */
    @Pointcut(value = "@annotation(org.wmframework.idempotent.annotations.Idempotent)")
    public void idempotent() {
    }

    @Around("idempotent()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("-----idempotent aspect start-----");
        Object[] args = pjp.getArgs();
        if (null == args || args.length == 0) {
            log.error("args is null，skip idempotent，execute target class");
            return pjp.proceed();
        }
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Idempotent idempotent = methodSignature.getMethod().getAnnotation(Idempotent.class);
        long timeout = idempotent.timeout();
        IdempotentStrategy strategy = idempotent.strategy();
        String keyStr = idempotentStrategyContext.accept(strategy, pjp);
        if (StringUtils.isEmpty(keyStr)) {
            log.error("keyStr is null,skip idempotent,execute target class");
            return pjp.proceed();
        }
        String key = new Md5Hash(JSON.toJSONString(keyStr)).toHex();
        log.info("redis key：{}", key);
        boolean setNxRes = setNx(key, "1", timeout);
        if (setNxRes) {
            log.info("try lock success,execute target class");
            Object processResult;
            try {
                processResult = pjp.proceed();
            } catch (Throwable e) {
                log.info("have exception,unlock key");
                stringRedisTemplate.delete(key);
                throw new BizException(e.getMessage());
            }
            String targetRes = JSONObject.toJSONString(processResult);
            log.info("target result:{}", targetRes);
            setEx(key, targetRes, timeout);
            return processResult;
        } else {
            log.info("try lock failed");
            String value = stringRedisTemplate.opsForValue().get(key);
            if ("1".equals(value)) {
                log.error("same request executing");
                throw new BizException("请求正在处理。。。。。。");
            } else {
                log.info("same request already be executed,return success result");
                //第一次已经处理完成，但未过超时时间，所以后续同样请求使用同一个返回结果
                return JSONObject.parseObject(value, Resp.class);
            }

        }
    }

    /**
     * 使用StringRedisTemplate实现setnx
     *
     * @param key    redis key
     * @param value  redis value  1
     * @param expire 设置的超时时间
     * @return true：setnx成功，false:失败或者执行结果为null
     */
    private boolean setNx(String key, String value, Long expire) {
        try {
            RedisCallback<Boolean> callback = (connection) ->
                    connection.set(key.getBytes(), value.getBytes(), Expiration.seconds(expire), RedisStringCommands.SetOption.SET_IF_ABSENT);
            Boolean result = stringRedisTemplate.execute(callback);
            return Optional.ofNullable(result).orElse(false);
        } catch (Exception e) {
            log.error("setNx redis occured an exception", e);
            return false;
        }
    }

    /**
     * 使用StringRedisTemplate实现setex
     *
     * @param key    redis key
     * @param value  redis value
     * @param expire 设置的超时时间
     * @return true：setex成功，false:失败或者执行结果为null
     */
    private boolean setEx(String key, String value, Long expire) {
        try {
            RedisCallback<Boolean> callback = (connection) ->
                    connection.set(key.getBytes(), value.getBytes(), Expiration.seconds(expire), RedisStringCommands.SetOption.SET_IF_PRESENT);
            Boolean result = stringRedisTemplate.execute(callback);
            return Optional.ofNullable(result).orElse(false);
        } catch (Exception e) {
            log.error("setEx redis occured an exception", e);
            return false;
        }
    }


}
