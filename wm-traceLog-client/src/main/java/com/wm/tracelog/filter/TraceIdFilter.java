package com.wm.tracelog.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 日志追踪拦截器
 * 通过requestHeader取出来traceId
 *
 * @author 王锰
 * @date 10:47 2019/8/8
 */
@WebFilter(filterName = "traceIdFilter", urlPatterns = "/*")
@Slf4j
public class TraceIdFilter implements Filter {

    private static final String TRACEID = "traceId";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("=======================traceIdFilter 启动=======================");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            MDC.put(TRACEID, request.getHeader(TRACEID));
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove(TRACEID);
        }
    }

    @Override
    public void destroy() {
        log.info("=======================traceIdFilter 销毁=======================");
    }
}
