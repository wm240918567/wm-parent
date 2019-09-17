package org.wmframework.idempotent.strategy;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.wmframework.idempotent.annotations.IdempotentField;
import org.wmframework.idempotent.common.IdempotentStrategy;
import org.wmframework.idempotent.dto.BaseIdempotentDto;
import org.wmframework.util.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * BASE_IDEMPOTENT_DTO策略
 * 不允许参数列表为空；
 * DTO必须继承于BaseIdempotentDto
 * DTO中如果存在IdempotentField表及字段；使用字段幂等；否则使用DTO幂等
 *
 * @author: 王锰
 * @date: 2018/8/20
 */
@Component("BASE_IDEMPOTENT_DTO")
@Slf4j
public class BaseDtoIdempotentStrategy implements IdempotentStrategyInterface {

    public static final String PREFIX = "base_idempotent_dto_";

    @Override
    public String process(ProceedingJoinPoint pjp) throws IllegalAccessException {
        log.info("idempotent strategy：{}", IdempotentStrategy.BASE_IDEMPOTENT_DTO.name());
        Object[] args = pjp.getArgs();
        Object dto = null;
        for (Object arg : args) {
            boolean assignableFrom = BaseIdempotentDto.class.isAssignableFrom(arg.getClass());
            if (assignableFrom) {
                dto = arg;
                break;
            }
        }
        if (dto == null) {
            log.info("not class extends of BaseIdempotentDto in list of parameter");
            return null;
        }
        StringBuilder keyStr = new StringBuilder(PREFIX);
        List<Field> fields = BeanUtils.recursive(new ArrayList<>(), dto.getClass());
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
            log.info("user idempotentField");
            return keyStr.substring(0, keyStr.length() - 1);
        }
        return keyStr.toString();
    }
}
