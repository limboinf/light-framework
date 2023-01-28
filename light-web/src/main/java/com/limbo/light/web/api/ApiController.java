package com.limbo.light.web.api;

import com.limbo.light.core.domain.web.IErrorCode;
import com.limbo.light.core.domain.web.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST api控制器
 *
 * @author limbo
 * @date 2023/01/28
 */
public class ApiController {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * 请求成功
     *
     * @param data 数据
     * @return {@link Result}<{@link T}>
     */
    protected <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 请求失败
     *
     * @param msg 提示内容
     * @return {@link Result}<{@link T}>
     */
    protected <T> Result<T> failed(String msg) {
        return Result.failed(msg);
    }

    /**
     * 请求失败
     *
     * @param errorCode 错误码
     * @return {@link Result}<{@link T}>
     */
    protected <T> Result<T> failed(IErrorCode errorCode) {
        return Result.failed(errorCode);
    }

    /**
     * 请求失败
     *
     * @param code 错误码
     * @param msg  提示内容
     * @return {@link Result}<{@link T}>
     */
    protected <T> Result<T> failed(int code, String msg) {
        return Result.failed(code, msg);
    }

}
