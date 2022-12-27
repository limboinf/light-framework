package com.limbo.light.lightlockspringbootstarter.core;

import com.limbo.light.lightlockspringbootstarter.lock.Lock;
import com.limbo.light.lightlockspringbootstarter.domain.LockInfo;
import org.aspectj.lang.JoinPoint;

/**
 * 获取锁超时处理策略
 *
 * @author limbo
 * @since 2022/12/26 14:01
 */
public interface LockTimeoutHandler {

    void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint);

}
