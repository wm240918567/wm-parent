package org.wmframework.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回值常量
 *
 * @author: 王锰
 * @date: 2019/8/18
 */
@Getter
@AllArgsConstructor
public enum RespConst {

    /**
     * 业务处理成功
     */
    OK(200, "业务处理成功"),
    /**
     * 请求异常
     */
    BAD_REQ(400, "请求异常"),
    /**
     * 业务处理失败
     */
    FAIL(500, "业务处理失败"),
    /**
     * 未知异常
     */
    UNKNOWN_EXCEPTION(501, "未知异常"),
    ;

    private Integer code;
    private String msg;
}
