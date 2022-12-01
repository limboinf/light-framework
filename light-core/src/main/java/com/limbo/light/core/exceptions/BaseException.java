package com.limbo.light.core.exceptions;


import com.limbo.light.core.domain.web.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 异常基类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {

    private int code = ResultCode.FAILED.getCode();
    private String message;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

}
