package org.wmframework.annotations;

import java.lang.annotation.*;


@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AddCache {

    /**
     * @return key前缀
     */
    String prefix() default "";

    /**
     * @return key超时时间；默认-1不过期删除
     */
    long timeout() default -1;

    /**
     * @return 是否自动刷新 默认不刷新
     */
    boolean autoRefresh() default false;

}
