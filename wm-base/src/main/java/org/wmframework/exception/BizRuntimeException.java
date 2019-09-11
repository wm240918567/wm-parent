package org.wmframework.exception;

import lombok.Getter;

/**
 * 业务运行时异常
 *
 * @author: 王锰
 * @date: 2018/8/18
 */
public class BizRuntimeException extends RuntimeException {

    @Getter
    private int errCode;

    public BizRuntimeException(String message) {
        super(message);
    }

    public BizRuntimeException(BizExceptionInfo info) {
        super(info.getErrMsg());
        this.errCode = info.getErrCode();
    }
}
