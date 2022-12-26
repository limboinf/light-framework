package com.limbo.light.lightlockspringbootstarter.support.lock;

/**
 * lock
 *
 * @author fangpeng
 * @since 2022/12/26 14:03
 */
public interface Lock {

    boolean acquire();

    boolean release();

}
