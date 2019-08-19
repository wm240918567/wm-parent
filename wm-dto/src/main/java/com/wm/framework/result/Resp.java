package com.wm.framework.result;

import com.wm.framework.common.RespConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 王锰
 * @date: 2019/8/18
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel(value = "通用业务返回对象Resp")
public class Resp<T> {

    @ApiModelProperty(value = "状态名称", name = "status", dataType = "String", required = true, example = "OK/BADREQ/FAIL")
    private String status;

    @ApiModelProperty(value = "状态编码", name = "code", dataType = "Integer", required = true, example = "200/500")
    private Integer code;

    @ApiModelProperty(value = "状态描述", name = "msg", dataType = "String", required = true, example = "业务处理成功/业务处理异常")
    private String msg;

    @ApiModelProperty(value = "返回对象,成功返回内容；异常返回异常信息", name = "body")
    private T body;

    /**
     * 处理成功，带返回值
     *
     * @param body 返回对象
     * @param <T>  对象泛型
     * @return Resp
     */
    public static <T> Resp<T> ok(final T body) {
        Resp<T> respBody = new Resp<>(RespConst.OK);
        respBody.setBody(body);
        return respBody;
    }

    /**
     * 处理成功，无返回值
     *
     * @return Resp
     */
    public static Resp ok() {
        return ok(null);
    }

    /**
     * 处理失败 带返回值
     *
     * @param body 返回对象
     * @param <T>  对象泛型
     * @return Resp
     */
    public static <T> Resp<T> fail(final T body) {
        Resp<T> respBody = new Resp<>(RespConst.FAIL);
        respBody.setBody(body);
        return respBody;
    }

    /**
     * 抛出未知异常
     *
     * @return 未知异常信息
     */
    public static <T> Resp<T> unknown(T body) {
        Resp<T> resp = new Resp<>(RespConst.UNKNOWN_EXCEPTION);
        resp.setBody(body);
        return resp;
    }

    /**
     * 处理失败 无返回值
     *
     * @return Resp
     */
    public static Resp fail() {
        return fail(null);
    }

    /**
     * 请求异常，带返回值
     *
     * @param body 返回对象
     * @param <T>  对象泛型
     * @return Resp
     */
    public static <T> Resp<T> badReq(final T body) {
        Resp<T> respBody = new Resp<>(RespConst.BAD_REQ);
        respBody.setBody(body);
        return respBody;
    }

    /**
     * 私有构造器不允许外部使用构造器新建对象
     *
     * @param respConst 返回常量
     */
    private Resp(final RespConst respConst) {
        this.status = respConst.name();
        this.code = respConst.getCode();
        this.msg = respConst.getMsg();
    }

}
