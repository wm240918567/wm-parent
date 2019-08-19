package com.wm.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 接口幂等，用于要添加幂等的方法
 * 一般用于controller层
 *
 * @author: 王锰
 * @date: 2018/8/18
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * 实现幂等参数名；为默认时，使用@requestBody做整体幂等
     *
     * @return 参数名数组
     */
    String[] value() default "";

    /**
     * key超时时间；默认时10秒超时
     *
     * @return 超时时间
     */
    long timeout() default 10;

    /**
     * 是否开启field幂等
     *
     * @return true:优先dto中字段做幂等，如果没有配置@IdempotentField，则使用dto做幂等；false：忽略@IdempotentField注解。
     */
    boolean enableField() default true;

}
