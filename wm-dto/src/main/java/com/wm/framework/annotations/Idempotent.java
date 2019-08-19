package com.wm.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 接口幂等
 *
 * @author: 王锰
 * @date: 2018/8/18
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    String value() default "";

    long timeout() default -1;

}
