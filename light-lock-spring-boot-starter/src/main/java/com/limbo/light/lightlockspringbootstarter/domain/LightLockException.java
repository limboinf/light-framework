package com.limbo.light.lightlockspringbootstarter.domain;

/**
 * lock exception
 *
 * @author limbo
 * @since 2022/12/26 15:34
 */
public class LightLockException extends RuntimeException {

    public LightLockException() {
    }

    public LightLockException(String message) {
        super(message);
    }

    public LightLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
