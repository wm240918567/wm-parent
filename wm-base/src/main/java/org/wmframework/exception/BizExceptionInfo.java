package org.wmframework.exception;

import lombok.Data;

/**
 * 业务异常信息封装类
 *
 * @author: 王锰
 * @date: 2019/9/11
 */
@Data
public class BizExceptionInfo {

    /**
     * 异常码
     */
    private int errCode;

    /**
     * 异常信息
     */
    private String errMsg;

}
