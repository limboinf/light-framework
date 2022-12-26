package com.limbo.light.lightlockspringbootstarter.support.annotation;

import com.limbo.light.lightlockspringbootstarter.support.core.LockTimeoutStrategy;
import com.limbo.light.lightlockspringbootstarter.support.core.ReleaseTimeoutStrategy;
import com.limbo.light.lightlockspringbootstarter.support.domain.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.limbo.light.lightlockspringbootstarter.support.config.LockConfig.DEFAULT_RELEASE_TIME;
import static com.limbo.light.lightlockspringbootstarter.support.config.LockConfig.DEFAULT_WAIT_TIME;

/**
 * 分布式锁注解
 *
 * @author limbo
 * @since 2022/12/26 16:08
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LightLock {

    /**
     * 锁名称
     *
     * @return {@link String}
     */
    String name() default "";

    /**
     * 锁类型, 默认是可重入锁
     *
     * @return {@link LockType}
     */
    LockType lockType() default LockType.Reentrant;

    /**
     * 尝试加锁的等待时间, 默认60s
     *
     * @return long
     */
    long waitTime() default DEFAULT_WAIT_TIME;

    /**
     * 释放锁的时间，默认60s
     *
     * @return long
     */
    long leaseTime() default DEFAULT_RELEASE_TIME;

    /**
     * 自定义业务key
     *
     * @return {@link String[]}
     */
    String[] keys() default {};

    /**
     * 锁超时策略, 默认不处理
     *
     * @return {@link LockTimeoutStrategy}
     */
    LockTimeoutStrategy lockTimeoutStrategy() default LockTimeoutStrategy.NO_OPERATION;

    /**
     * 释放超时策略, 默认不处理
     *
     * @return {@link ReleaseTimeoutStrategy}
     */
    ReleaseTimeoutStrategy releaseTimeoutStrategy() default ReleaseTimeoutStrategy.NO_OPERATION;

    /**
     * 自定义锁定超时策略
     *
     * @return {@link String}
     */
    String customLockTimeoutStrategy() default "";

    /**
     * 自定义释放超时策略
     *
     * @return {@link String}
     */
    String customReleaseTimeoutStrategy() default "";

}
