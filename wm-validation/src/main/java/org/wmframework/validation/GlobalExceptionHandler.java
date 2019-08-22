package org.wmframework.validation;

import com.alibaba.fastjson.JSONObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wmframework.exception.BizException;
import org.wmframework.exception.BizRuntimeException;
import org.wmframework.result.Resp;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author: 王锰
 * @date: 2019/08/16
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常拦截器
     *
     * @param ex BizException异常
     * @return 返回值
     */
    @ExceptionHandler(BizException.class)
    public Resp<String> bizExceptionHandler(BizException ex) {
        log.error("业务异常：{}", ex.getMessage());
        return Resp.fail(ex.getMessage());
    }

    /**
     * 业务异常拦截器
     *
     * @param ex BizRuntimeException异常
     * @return 返回值
     */
    @ExceptionHandler(BizRuntimeException.class)
    public Resp<String> bizRuntimeExceptionHandler(BizRuntimeException ex) {
        log.error("业务运行时异常：{}", ex.getMessage());
        return Resp.fail(ex.getMessage());
    }

//    /**
//     * 全局未知异常拦截器
//     *
//     * @param ex Throwable异常
//     * @return 返回值
//     */
//    @ExceptionHandler({Throwable.class})
//    public Resp<String> throwableHandler(Throwable ex) {
//        String bodyMsg;
//        if (ex instanceof NullPointerException && StringUtils.isEmpty(ex.getMessage())) {
//            //对NPE的特殊处理
//            bodyMsg = "NPE";
//        } else {
//            bodyMsg = ex.getMessage();
//        }
//        log.error("未知异常：{}", bodyMsg);
//        ex.printStackTrace();
//        return Resp.unknown(bodyMsg);
//    }


    /**
     * 启用@valid校验抛出异常处理
     *
     * @param ex 异常信息
     * @return 包装过后的异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Resp<ValidExceptionInfo> validationErrorHandler(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        log.error("请求参数未通过validation校验，异常全部信息：{}", JSONObject.toJSONString(allErrors, true));
        List<String> defaultMessageList = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        ValidExceptionInfo build = ValidExceptionInfo.builder().exMsg(defaultMessageList).build();
        return Resp.badReq(build);
    }

    @Data
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ValidExceptionInfo {
        private List<String> exMsg;
    }

}
