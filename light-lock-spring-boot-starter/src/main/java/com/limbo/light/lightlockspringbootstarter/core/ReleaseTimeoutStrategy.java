package com.limbo.light.lightlockspringbootstarter.core;

import com.limbo.light.lightlockspringbootstarter.domain.LockTimeoutException;
import com.limbo.light.lightlockspringbootstarter.domain.LockInfo;

/**
 * 锁释放异常处理策略
 *
 * @author limbo
 * @since 2022/12/26 16:03
 */
public enum ReleaseTimeoutStrategy implements ReleaseTimeoutHandler {

    /**
     * 不做任何处理
     */
    NO_OPERATION() {
        @Override
        public void handle(LockInfo lockInfo) {
            // do nothing.
        }
    },

    /**
     * 快速失败
     */
    FAIL_FAST() {
        @Override
        public void handle(LockInfo lockInfo) {
            throw new LockTimeoutException(
                    String.format("Release lock error, lockName: %s, leaseTime: %ds", lockInfo.getName(), lockInfo.getLeaseTime()));
        }
    }
}
