package org.wmframework.idempotent.annotations;

import org.wmframework.idempotent.common.IdempotentStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 幂等注解
 * 用于controller层方法
 *
 * @author: 王锰
 * @date: 2018/8/18
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * @return key超时时间；默认10秒超时
     */
    long timeout() default 10;

    /**
     * @return 幂等策略
     */
    IdempotentStrategy strategy() default IdempotentStrategy.BASE_IDEMPOTENT_DTO;

}
