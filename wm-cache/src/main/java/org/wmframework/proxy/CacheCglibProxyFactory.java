package org.wmframework.proxy;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class CacheCglibProxyFactory<T> implements MethodInterceptor {

    private T target;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private CacheCglibProxyFactory(T target) {
        this.target = target;
    }


    public static <T> CacheCglibProxyFactory<T> instance(T target) {
        return new CacheCglibProxyFactory<>(target);
    }

    //给目标对象创建一个代理对象
    @SuppressWarnings("unchecked")
    public T getProxy() {
        //1.工具类
        Enhancer en = new Enhancer();
        //2.设置父类
        en.setSuperclass(target.getClass());
        //3.设置回调函数
        en.setCallback(this);
        //4.创建子类(代理对象)
        return (T) en.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object invoke = method.invoke(target, args);
        stringRedisTemplate.opsForValue().set(JSONObject.toJSONString(args), JSONObject.toJSONString(invoke));
        return invoke;
    }
}
