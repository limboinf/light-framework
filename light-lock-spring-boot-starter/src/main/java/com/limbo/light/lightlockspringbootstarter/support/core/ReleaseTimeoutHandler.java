package com.limbo.light.lightlockspringbootstarter.support.core;

import com.limbo.light.lightlockspringbootstarter.support.domain.LockInfo;
import com.limbo.light.lightlockspringbootstarter.support.lock.Lock;
import org.aspectj.lang.JoinPoint;

/**
 * 锁释放超时处理策略
 *
 * @author limbo
 * @since 2022/12/26 14:01
 */
public interface ReleaseTimeoutHandler {

    void handle(LockInfo lockInfo);

}
