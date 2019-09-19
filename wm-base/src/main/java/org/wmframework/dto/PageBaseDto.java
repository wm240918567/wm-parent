package org.wmframework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 基础分页查询对象
 *
 * @author: 王锰
 * @date: 2019/8/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "基础分页查询对象", description = "需要分页DTO的基类")
public class PageBaseDto extends BaseDto {

    @ApiModelProperty(value = "页数,填【0】则为不分页", name = "pageNum", dataType = "Integer", example = "1", required = true)
    @NotNull(message = "页数[pageNum]{notnull}")
    @Min(value = 0, message = "页数[pageNum]{num.min}")
    protected Integer pageNum;

    @ApiModelProperty(value = "分页大小", name = "pageSize", dataType = "Integer", example = "50", required = true)
    @NotNull(message = "分页大小[pageSize]{notnull}")
    @Min(value = 1, message = "分页大小[pageSize]{num.min}")
    protected Integer pageSize;

}
