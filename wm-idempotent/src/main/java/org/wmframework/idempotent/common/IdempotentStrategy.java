package org.wmframework.idempotent.common;

import lombok.Getter;

/**
 * 幂等策略枚举
 *
 * @author: 王锰
 * @date: 2018/08/20
 */
@Getter
public enum IdempotentStrategy {

    /**
     * 使用继承与BaseDto的类，如果dto中有指定字段，使用指定字段；否则使用dto整体做幂等
     */
    BASE_IDEMPOTENT_DTO,

    /**
     * 使用实现了IdempotentInterface接口的类做幂等；如果dto中有指定字段，使用指定字段；否则使用dto整体做幂等
     */
    IDEMPOTENT_INTERFACE,

    /**
     * 使用整个参数列表做幂等，无关@idempotentfield
     */
    LIST_PARAMETER,
    ;

}
