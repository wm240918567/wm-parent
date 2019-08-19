package com.wm.framework.idempotent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wm.framework.annotations.Idempotent;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class IdempotentAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 切点，以Controller结尾的类中所有public级别的方法
     */
    @Pointcut(value = "@annotation(com.wm.framework.annotations.Idempotent)")
    public void idempotent() {
    }

    @Around("idempotent()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Idempotent idempotent = methodSignature.getMethod().getAnnotation(Idempotent.class);
        long timeout = idempotent.timeout();
        Optional<Object> requestBodyOptional = Arrays.stream(args).filter(arg ->
                null != arg.getClass().getAnnotation(RequestBody.class)).findFirst();
        Object dto = requestBodyOptional.orElse(args[0]);
        String key = new Md5Hash(JSON.toJSONString(dto)).toHex();
        Object value = redisTemplate.opsForValue().get(key);
        //不存在key，可以进入
        if (null == key) {
            Object processResult = pjp.proceed();
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(processResult), timeout, TimeUnit.SECONDS);
            return processResult;
        } else {
            //存在key，不进入方法。取redis返回值
            return value;
        }
    }

}
