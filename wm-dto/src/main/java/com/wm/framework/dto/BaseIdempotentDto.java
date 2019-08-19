package com.wm.framework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BaseIdempotentDto对象", description = "需要使用messageId实现幂等的dto对象基类")
public class BaseIdempotentDto extends BaseDto {

    @ApiModelProperty(value = "消息ID,用于实现幂等操作", name = "messageId", dataType = "String")
    protected String messageId;

}
