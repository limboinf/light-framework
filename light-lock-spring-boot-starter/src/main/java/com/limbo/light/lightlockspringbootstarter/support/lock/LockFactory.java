package com.limbo.light.lightlockspringbootstarter.support.lock;

import com.limbo.light.lightlockspringbootstarter.support.domain.LockInfo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * lock factory
 *
 * @author limbo
 * @since 2022/12/26 14:59
 */
public class LockFactory {

    @Autowired
    private RedissonClient redissonClient;

    public Lock getLockInstance(LockInfo lockInfo) {
        switch (lockInfo.getLockType()) {
            case Fair:
                return new FairLock(lockInfo, redissonClient);
            case Reentrant:
            default:
                return new ReentrantLock(lockInfo, redissonClient);
        }
    }

}
