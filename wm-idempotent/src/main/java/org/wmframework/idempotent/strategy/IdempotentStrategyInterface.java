package org.wmframework.idempotent.strategy;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 接口幂等keyStr实现策略
 *
 * @author: 王锰
 * @date: 2018/8/20
 */
public interface IdempotentStrategyInterface {

    /**
     * 根据不同策略获取key值
     *
     * @return key值
     */
    String process(ProceedingJoinPoint pjp) throws IllegalAccessException;

}
