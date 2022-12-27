package com.limbo.light.lightlockspringbootstarter.lock;

/**
 * lock
 *
 * @author limbo
 * @since 2022/12/26 14:03
 */
public interface Lock {

    boolean acquire();

    boolean release();

}
