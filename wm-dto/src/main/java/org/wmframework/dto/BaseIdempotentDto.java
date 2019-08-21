package org.wmframework.dto;

import org.wmframework.annotations.IdempotentField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "BaseIdempotentDto对象", description = "需要使用messageId实现幂等的dto对象基类")
public class BaseIdempotentDto extends BaseDto {

    @IdempotentField
    @ApiModelProperty(value = "消息ID,用于实现幂等操作", name = "messageId", dataType = "String")
    protected String messageId;

}
