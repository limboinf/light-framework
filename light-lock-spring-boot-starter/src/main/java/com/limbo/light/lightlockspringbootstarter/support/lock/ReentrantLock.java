package com.limbo.light.lightlockspringbootstarter.support.lock;

import com.limbo.light.lightlockspringbootstarter.support.domain.LockInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 可重入锁(默认）
 *
 * @author limbo
 * @since 2022/12/26 14:07
 */
@Slf4j
public class ReentrantLock extends AbsLock {

    private final LockInfo lockInfo;
    private final RedissonClient redissonClient;

    public ReentrantLock(LockInfo lockInfo, RedissonClient redissonClient) {
        super(lockInfo, redissonClient);
        this.lockInfo = lockInfo;
        this.redissonClient = redissonClient;
    }

    @Override
    public RLock getLock() {
        return redissonClient.getLock(lockInfo.getName());
    }
}
