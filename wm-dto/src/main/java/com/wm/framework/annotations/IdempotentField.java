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
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdempotentField {

}
