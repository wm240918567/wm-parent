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
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.wmframework.annotations.Idempotent;
import org.wmframework.annotations.IdempotentField;
import org.wmframework.exception.BizException;
import redis.clients.jedis.JedisCommands;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 切点，标注了@Idempotent的controller方法
     */
    @Pointcut(value = "@annotation(org.wmframework.annotations.Idempotent)")
    public void idempotent() {
    }

    @Around("idempotent()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Idempotent idempotent = methodSignature.getMethod().getAnnotation(Idempotent.class);
        long timeout = idempotent.timeout();
        boolean enableField = idempotent.enableField();

        /*
          准备key，value
          key使用参数列表值或者指定字段值MD5
          value 1
          setnx true：第一次请求可进，加锁；（xxxx=1） false：不是第一次并且锁没释放，拒绝：正在处理。(xxxx=1)
          处理结束，setex（xxxx=结果） 设置超时时间

         */
        StringBuilder keyStr = getKeyStr(args, enableField);
        String key = new Md5Hash(JSON.toJSONString(keyStr)).toHex();

        boolean setNxRes = tryLock(key, "1", timeout);
        if (setNxRes) {
            Object processResult = pjp.proceed();
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(processResult), timeout, TimeUnit.SECONDS);
            return processResult;
        } else {
            String value = redisTemplate.opsForValue().get(key);
            if ("1".equals(value)) {
                throw new BizException("请求正在处理。。。。。。");
            } else {
                //第一次已经处理完成，但未过超时时间，所以后续同样请求使用同一个返回结果
                return value;
            }
        }
    }

    /**
     * 1，判断是否启用field
     * a，启用：找被@RequestBody标记的dto。未找到使用b
     * b，未启用，启用参数列表做整体拼串
     *
     * @param args        参数列表
     * @param enableField 是否开启field匹配
     * @return 拼的key
     * @throws IllegalAccessException
     */
    private StringBuilder getKeyStr(Object[] args, boolean enableField) throws IllegalAccessException {
        StringBuilder keyStr = new StringBuilder("idempotent_");
        Object dto = null;
        //启用字段
        if (enableField) {
            for (Object arg : args) {
                RequestBody requestBody = arg.getClass().getAnnotation(RequestBody.class);
                if (null != requestBody) {
                    dto = arg;
                    break;
                }
            }
            //使用参数列表
            if (null == dto) {
                keyStr.append(JSONObject.toJSONString(Arrays.asList(args)));
            }
            //使用dto中标注字段
            else {
                Field[] fields = dto.getClass().getFields();
                for (Field field : fields) {
                    IdempotentField idempotentField = field.getAnnotation(IdempotentField.class);
                    if (null == idempotentField) {
                        continue;
                    }
                    keyStr.append(field.get(dto)).append("_");
                }
                keyStr.substring(0, keyStr.length() - 1);
            }
        }
        //使用参数列表
        else {
            keyStr.append(JSONObject.toJSONString(Arrays.asList(args)));
        }
        return keyStr;
    }

    /**
     * 使用jedis实现setnx时添加超时时间
     *
     * @param key    拼好的key
     * @param value  1
     * @param expire 设置的超时时间
     * @return true：setnx成功，飞一次
     */
    public boolean tryLock(String key, String value, Long expire) {
        try {
            RedisCallback<String> callback = (connection) -> {
                JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                return commands.set(key, value, "NX", "PX", expire);
            };

            String result = redisTemplate.execute(callback);

            return !StringUtils.isEmpty(result);
        } catch (Exception e) {
            log.error("set redis occured an exception", e);
        }
        return false;
    }


}
