package org.wmframework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础数据传输对象
 *
 * @author: 王锰
 * @date: 2019/8/18
 */
@ApiModel(value = "基础数据传输对象", description = "DTO的基类")
@Data
public class BaseDto implements Serializable {

    @NotBlank(message = "操作人不能为空")
    @ApiModelProperty(value = "操作人", name = "operationBy", required = true, example = "wm")
    protected String operationBy;

    @NotNull(message = "操作时间不能为空")
    @ApiModelProperty(value = "操作时间", name = "operationDate", required = true)
    protected LocalDateTime operationDate;

}
