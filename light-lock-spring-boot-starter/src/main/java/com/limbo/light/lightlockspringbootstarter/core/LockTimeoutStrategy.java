package com.limbo.light.lightlockspringbootstarter.core;

import com.limbo.light.lightlockspringbootstarter.domain.LockInfo;
import com.limbo.light.lightlockspringbootstarter.domain.LockTimeoutException;
import com.limbo.light.lightlockspringbootstarter.lock.Lock;
import org.aspectj.lang.JoinPoint;

import java.util.concurrent.TimeUnit;

/**
 * 锁超时处理策略
 *
 * @author limbo
 * @since 2022/12/26 15:31
 */
public enum LockTimeoutStrategy implements LockTimeoutHandler {

    /**
     * 不做任何处理
     */
    NO_OPERATION() {
        @Override
        public void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {
            // do nothing.
        }
    },

    /**
     * 快速失败
     */
    FAIL_FAST() {
        @Override
        public void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {
            throw new LockTimeoutException(
                    String.format("Try acquire lock error: lockKey:%s, timeout:%ds", lockInfo.getName(), lockInfo.getWaitTime()));
        }
    },

    /**
     * 阻塞, 尝试获取锁直到超过最大尝试限制
     */
    BLOCKING() {

        private static final long DEFAULT_INTERVAL = 100L;
        private static final long DEFAULT_MAX_INTERVAL = 3 * 60 * 1000L;

        @Override
        public void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {
            long interval = DEFAULT_INTERVAL;
            while (lock.acquire()) {
                if (interval > DEFAULT_MAX_INTERVAL) {
                    throw new LockTimeoutException(
                            String.format("Too many times to try acquire lock error: lockKey:%s, timeout:%ds", lockInfo.getName(), lockInfo.getWaitTime()));
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(interval);
                    interval <<= 1;
                } catch (InterruptedException e) {
                    throw new LockTimeoutException("Failed to get lock", e);
                }
            }
        }
    }


}
