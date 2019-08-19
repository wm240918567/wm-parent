//package com.wm.framework.idempotent;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.wm.framework.annotations.Idempotent;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.crypto.hash.Md5Hash;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.util.Arrays;
//import java.util.Optional;
//import java.util.concurrent.TimeUnit;
//
///**
// * 接口幂等切面
// * 1,方法上使用Idempotent注解，被标注的类被切dao
// * 2,找到实现幂等的字段：
// *  a,没有被@requestBody标注的dto，使用idempotent+全限定类名+方法名+参数列表转list后序列化json字符串做幂等
// *  b,有被@requestBody标注的dto且enableField==true,dto对象中没有字段被@IdempotentField标注的，拿idempotent+dto序列化json的字符串做幂等
// *  c,有被@requestBody标注的dto，且dto中有被@IdempotentField标注的，拿idempotent+dto中被指定的字段拼的string做幂等
// *
// * @author: 王锰
// * @date: 2019/8/18
// */
//@Aspect
//@Component
//@Slf4j
//public class IdempotentAspect {
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    /**
//     * 切点，标注了@Idempotent的controller方法
//     */
//    @Pointcut(value = "@annotation(com.wm.framework.annotations.Idempotent)")
//    public void idempotent() {
//    }
//
//    @Around("idempotent()")
//    public Object around(ProceedingJoinPoint pjp) throws Throwable {
//        Object[] args = pjp.getArgs();
//        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
//        Idempotent idempotent = methodSignature.getMethod().getAnnotation(Idempotent.class);
//        String[] idempotentValue = idempotent.value();
//        if(idempotentValue.length==0){
//            //使用dto做幂等
//            Optional<Object> requestBodyOptional = Arrays.stream(args).filter(arg ->
//                    null != arg.getClass().getAnnotation(RequestBody.class)).findFirst();
//            Object dto = requestBodyOptional.orElse(args[0]);
//            String key = new Md5Hash(JSON.toJSONString(dto)).toHex();
//            Object value = redisTemplate.opsForValue().get(key);
//        }else{
//
//            for (Object arg : args) {
//                arg
//            }
//        }
//        long idempotentTimeout = idempotent.timeout();
//        //不存在key，可以进入
//        if (null == key) {
//            Object processResult = pjp.proceed();
//            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(processResult), idempotentTimeout, TimeUnit.SECONDS);
//            return processResult;
//        } else {
//            //存在key，不进入方法。取redis返回值
//            return value;
//        }
//    }
//
//}
