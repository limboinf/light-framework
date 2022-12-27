package com.limbo.light.lightlockspringbootstarter.core;

import com.limbo.light.lightlockspringbootstarter.domain.LockInfo;

/**
 * 锁释放超时处理策略
 *
 * @author limbo
 * @since 2022/12/26 14:01
 */
public interface ReleaseTimeoutHandler {

    void handle(LockInfo lockInfo);

}
