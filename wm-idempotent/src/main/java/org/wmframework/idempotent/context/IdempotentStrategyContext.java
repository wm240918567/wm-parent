package org.wmframework.idempotent.context;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.wmframework.idempotent.common.IdempotentStrategy;
import org.wmframework.idempotent.strategy.IdempotentStrategyInterface;

/**
 * 幂等策略转换器
 *
 * @author 王锰
 * @date 20:03 2019/7/8
 */
@Component
public class IdempotentStrategyContext extends AbstractStrategyContext<IdempotentStrategyInterface> {

    /**
     * 策略转换方法
     */
    public <T> String accept(IdempotentStrategy idempotentStrategy, ProceedingJoinPoint pjp) throws IllegalAccessException {
        return getStrategy(idempotentStrategy.name()).process(pjp);
    }
}
