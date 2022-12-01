package com.limbo.light.core.domain.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 常用操作码
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IErrorCode {

    SUCCESS(0, "Success"),
    FAILED(-1, "Fail"),

    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    PARAM_VALIDATE_FAILED(400, "参数检验失败");

    private int code;
    private String msg;

    @Override
    public String getMessage() {
        return msg;
    }
}
