package com.limbo.light.lightlockspringbootstarter.lock;

import com.limbo.light.lightlockspringbootstarter.domain.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 公平锁
 *
 * @author limbo
 * @since 2022/12/26 15:03
 */
public class FairLock extends AbsLock {

    private final LockInfo lockInfo;
    private final RedissonClient redissonClient;

    public FairLock(LockInfo lockInfo, RedissonClient redissonClient) {
        super(lockInfo);
        this.lockInfo = lockInfo;
        this.redissonClient = redissonClient;
    }

    @Override
    public RLock getLock() {
        return redissonClient.getFairLock(lockInfo.getName());
    }
}
