package org.wmframework.idempotent.strategy;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.wmframework.idempotent.common.IdempotentStrategy;

import java.util.Arrays;

/**
 * LIST_PARAMETER策略
 * 不允许参数列表为空
 *
 * @author: 王锰
 * @date: 2018/8/20
 */
@Component("LIST_PARAMETER")
@Slf4j
public class ListParameterIdempotentStrategy implements IdempotentStrategyInterface {

    public static final String PREFIX = "list_parameter_";

    @Override
    public String process(ProceedingJoinPoint pjp) {
        log.info("idempotent strategy:{}", IdempotentStrategy.LIST_PARAMETER.name());
        Object[] args = pjp.getArgs();
        StringBuilder keyStr = new StringBuilder(PREFIX);
        keyStr.append(JSONObject.toJSONString(Arrays.asList(args)));
        return keyStr.toString();
    }
}
