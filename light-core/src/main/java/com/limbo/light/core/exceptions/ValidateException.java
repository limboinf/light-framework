package com.limbo.light.core.exceptions;


import com.limbo.light.core.domain.web.ResultCode;

/**
 * 验证异常
 */
public class ValidateException extends BaseException {

    public ValidateException(String message) {
        super(ResultCode.PARAM_VALIDATE_FAILED.getCode(), message);
    }

    public ValidateException(int code, String message) {
        super(code, message);
    }

    /**
     * 通用参数校验异常
     */
    public static final ValidateException COMMON_VALIDATE_EXCEPTION = new ValidateException("COMMON_VALIDATE_EXCEPTION");

    
}
