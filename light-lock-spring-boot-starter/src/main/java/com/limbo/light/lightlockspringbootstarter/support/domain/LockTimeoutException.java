package com.limbo.light.lightlockspringbootstarter.support.domain;

/**
 * lock timeout exception
 *
 * @author limbo
 * @since 2022/12/26 15:34
 */
public class LockTimeoutException extends RuntimeException {

    public LockTimeoutException() {
    }

    public LockTimeoutException(String message) {
        super(message);
    }

    public LockTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
