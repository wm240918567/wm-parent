package com.wm.framework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: 王锰
 * @date: 2019/8/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "基础分页查询对象", description = "需要分页时必须继承此DTO")
public class PageBaseDto extends BaseDto {

    @ApiModelProperty(value = "页数", name = "页数")
    protected int pageNum;

    @ApiModelProperty(value = "分页大小", name = "分页大小")
    protected int pageSize;

}
