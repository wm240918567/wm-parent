package org.wmframework.exception;

/**
 * 运行时异常
 *
 * @author: 王锰
 * @date: 2018/8/18
 */
public class BizRuntimeException extends RuntimeException {

    public BizRuntimeException(String message) {
        super(message);
    }
}
