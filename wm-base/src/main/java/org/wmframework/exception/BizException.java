package org.wmframework.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author: 王锰
 * @date: 2018/8/18
 */
public class BizException extends Exception {

    @Getter
    private int errCode;

    /**
     * 只抛出异常信息
     *
     * @param message 自定义异常信息
     */
    public BizException(String message) {
        super(message);
    }

    /**
     * 抛出附带异常码的异常信息
     *
     * @param info 各子系统自定义的异常信息对象
     */
    public BizException(BizExceptionInfo info) {
        super(info.getErrMsg());
        this.errCode = info.getErrCode();
    }
}
