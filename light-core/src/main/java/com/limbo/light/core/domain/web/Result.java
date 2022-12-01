package com.limbo.light.core.domain.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private long code;

    // 提示信息，仅供接口出错使用
    private String message;

    // 返回数据
    private T data;

    /**
     * 成功返回结果
     *
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    public static <T> Result<T> failed(IErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param data 获取的数据
     */
    public static <T> Result<T> failed(IErrorCode errorCode, T data) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), data);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误代码
     * @param message   消息
     * @return {@link Result}<{@link T}>
     */
    public static <T> Result<T> failed(int errorCode, String message) {
        return new Result<>(errorCode, message, null);
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     * @param code 错误码
     * @param message 提示信息
     */
    public static <T> Result<T> failed(long code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return failed(ResultCode.PARAM_VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result<>(ResultCode.PARAM_VALIDATE_FAILED.getCode(), message, null);
    }

}

