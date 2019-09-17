package org.wmframework.permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {

    /**
     * @return 接口名；不填默认使用方法名
     */
    String name() default "";

    /**
     * @return 动态修改接口权限
     */
    boolean dynamic() default false;

    /**
     * @return 角色
     */
    Role role() default Role.SUPER_ADMIN;


}
