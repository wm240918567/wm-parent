package com.wm.framework.validation;


import com.alibaba.fastjson.JSONObject;
import com.wm.framework.result.Resp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
