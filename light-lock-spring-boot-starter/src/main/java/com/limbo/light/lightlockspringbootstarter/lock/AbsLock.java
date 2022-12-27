package com.limbo.light.lightlockspringbootstarter.lock;

import com.limbo.light.lightlockspringbootstarter.domain.LockInfo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            boolean b = rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
            log.debug("AbsLock lock acquire key: {} status: {}", getKey(), b);
            return b;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        if (Objects.isNull(rLock)) {
            log.debug("AbsLock release key: {} successful cause rLock is null.", getKey());
            return true;
        }

        // 判断要解锁的key是否已被锁定, 如设置leaseTime在调用该方法前已自动过期
        if (!rLock.isLocked()) {
            log.debug("AbsLock release key: {} successful cause rLock is unlock.", getKey());
            return true;
        }

        // 判断要解锁的key是否被当前线程持有
        if (rLock.isHeldByCurrentThread()) {
            try {
                rLock.forceUnlockAsync().get();
                log.debug("AbsLock release key: {} successful", getKey());
                return true;
            } catch (InterruptedException | ExecutionException e) {
                log.warn("AbsLock release key: {} failed", getKey(), e);
                return false;
            }
        }

        log.warn("AbsLock release key: {} fail cause not current thread.", getKey());
        return false;
    }
}
