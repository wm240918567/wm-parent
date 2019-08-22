package org.wmframework.idempotent.strategy;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.wmframework.idempotent.annotations.IdempotentField;
import org.wmframework.idempotent.common.IdempotentStrategy;
import org.wmframework.idempotent.interfaces.IdempotentInterface;

import java.lang.reflect.Field;

/**
 * IDEMPOTENT_INTERFACE策略
 * 不允许参数列表为空；
 * DTO必须实现IdempotentInterface接口；
 * DTO中如果存在IdempotentField表及字段；使用字段幂等；否则使用DTO幂等
 *
 * @author: 王锰
 * @date: 2018/8/20
 */
@Component("IDEMPOTENT_INTERFACE")
@Slf4j
public class InterfacesIdempotentStrategy implements IdempotentStrategyInterface {

    public static final String PREFIX = "idempotent_interface_";

    @Override
    public String process(ProceedingJoinPoint pjp) throws IllegalAccessException {
        log.info("idempotent strategy：{}", IdempotentStrategy.IDEMPOTENT_INTERFACE.name());
        Object[] args = pjp.getArgs();
        Object dto = null;
        for (Object arg : args) {
            boolean assignableFrom = IdempotentInterface.class.isAssignableFrom(arg.getClass());
            if (assignableFrom) {
                dto = arg;
                break;
            }
        }
        if (dto == null) {
            log.info("no class implements of IdempotentStrategyInterface in list of parameter");
            return null;
        }
        StringBuilder keyStr = new StringBuilder(PREFIX);
        Field[] fields = dto.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            IdempotentField idempotentField = field.getAnnotation(IdempotentField.class);
            if (null == idempotentField) {
                continue;
            }
            keyStr.append(field.get(dto)).append("_");
        }
        if (keyStr.toString().equals(PREFIX)) {
            log.info("use dto");
            keyStr.append(JSONObject.toJSONString(dto));
        } else {
            log.info("use idempotentField");
            keyStr.substring(0, keyStr.length() - 1);
        }
        return keyStr.toString();
    }
}
