package org.wmframework.tracelog.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.wmframework.tracelog.common.TraceIdConst;

/**
 * 使用feign进行服务间调用时
 * 通过实现RequestInterceptor接口
 * 对发送的request的header添加traceId
 * 实现日志追踪
 *
 * @author 王锰
 * @date 17:52 2019/8/8
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(TraceIdConst.TRACE_ID, MDC.get(TraceIdConst.TRACE_ID));
    }
}

