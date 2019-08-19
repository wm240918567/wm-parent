package com.wm.framework.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Resp返回常量
 */
@Getter
@AllArgsConstructor
public enum RespConst {

    OK(200, "业务处理成功"),
    BADREQ(400, "请求异常"),
    FAIL(500, "业务处理异常"),
    ;

    private Integer code;
    private String msg;
}
