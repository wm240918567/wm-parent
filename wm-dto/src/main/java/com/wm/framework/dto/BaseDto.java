package com.wm.framework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel(value = "基础数据传输对象",description = "所有dto继承此DTO")
@Data
public class BaseDto implements Serializable {

    @NotBlank(message = "操作人不能为空")
    @ApiModelProperty(value = "操作人",name = "operationBy",required = true,example = "wm")
    protected String operationBy;

    @NotNull(message = "操作时间不能为空")
    @ApiModelProperty(value = "操作时间",name = "operationDate",required = true)
    protected LocalDateTime operationDate;

}
