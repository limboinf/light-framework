package com.limbo.light.lightlockspringbootstarter.domain;

/**
 * lock leave timeout exception
 *
 * @author limbo
 * @since 2022/12/26 15:34
 */
public class LeaveTimeoutException extends RuntimeException {

    public LeaveTimeoutException() {
    }

    public LeaveTimeoutException(String message) {
        super(message);
    }

    public LeaveTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
