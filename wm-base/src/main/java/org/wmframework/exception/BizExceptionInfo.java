package org.wmframework.exception;

import lombok.Builder;
import lombok.Data;

/**
 * 业务异常信息封装类
 *
 * @author: 王锰
 * @date: 2019/9/11
 */
@Data
@Builder
public class BizExceptionInfo {

    /**
     * 异常码
     */
    private final int errCode;

    /**
     * 异常信息
     */
    private final String errMsg;


}
