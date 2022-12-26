package com.limbo.light.lightlockspringbootstarter.support.lock;

import com.limbo.light.lightlockspringbootstarter.support.domain.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * abstract lock
 *
 * @author fangpeng
 * @since 2022/12/26 14:21
 */
public abstract class AbsLock implements Lock {

    private RLock rLock;
    private final LockInfo lockInfo;
    private final RedissonClient redissonClient;

    protected AbsLock(LockInfo lockInfo, RedissonClient redissonClient) {
        this.lockInfo = lockInfo;
        this.redissonClient = redissonClient;
    }

    public String getKey() {
        return lockInfo.getName();
    }

    public abstract RLock getLock();

    @Override
    public boolean acquire() {
        try {
            // 尝试加锁，最多等待 waitTime秒，上锁后 leaseTime秒后自动解锁
            rLock = getLock();
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        if (Objects.isNull(rLock)) {
            return true;
        }

        if (rLock.isHeldByCurrentThread()) {
            try {
                rLock.forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }
        return false;
    }
}
