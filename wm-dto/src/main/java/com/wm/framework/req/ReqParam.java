package com.wm.framework.req;

import lombok.Data;

import java.util.Map;

@Data
public class ReqParam<T> {

    private Map<String, Object> pathParam;

    private Map<String, Object> requestParameter;

    private T body;

}
