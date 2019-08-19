//package com.wm.framework.aspect;
//
//
//import com.alibaba.fastjson.JSONObject;
//import com.wm.framework.result.Resp;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Aspect
//@Component
//@Slf4j
//public class ReqParamAspect {
//
//    /**
//     * 切点，以Controller结尾的类中所有public级别的方法
//     */
//    @Pointcut(value = "execution(public * *Controller.*(..))")
//    public void pointcut() {
//    }
//
//    @Around("pointcut()")
//    public Object around(ProceedingJoinPoint pjp) throws Exception {
//        Object[] args = pjp.getArgs();
//        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
//        String[] parameterNames = methodSignature.getParameterNames();
//
//        String requestBodyStr = null;
//        Map<String, Object> pathVariableMap = new HashMap<>(args.length);
//        Map<String, Object> requestParameterMap = new HashMap<>();
//        for (Object arg : args) {
//            RequestBody requestBody = arg.getClass().getAnnotation(RequestBody.class);
//            if (null != requestBody) {
//                requestBodyStr = JSONObject.toJSONString(arg);
//                continue;
//            }
//            PathVariable pathVariable = arg.getClass().getAnnotation(PathVariable.class);
//            if (null != pathVariable) {
//
//            }
//            System.out.println();
//        }
//        try {
//            Resp processResult = (Resp) pjp.proceed();
//            processResult.setReqParam(requestBodyStr);
//            return processResult;
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }
//
//}
