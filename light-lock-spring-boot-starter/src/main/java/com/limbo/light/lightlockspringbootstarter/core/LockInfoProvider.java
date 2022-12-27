package com.limbo.light.lightlockspringbootstarter.core;

import com.limbo.light.lightlockspringbootstarter.annotation.LightLock;
import com.limbo.light.lightlockspringbootstarter.config.LockConfig;
import com.limbo.light.lightlockspringbootstarter.domain.LockInfo;
import com.limbo.light.lightlockspringbootstarter.domain.LockType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 获取LockInfo
 *
 * @author limbo
 * @since 2022/12/27 10:17
 */
@Slf4j
public class LockInfoProvider {

    private static final String LOCK_NAME_PREFIX = "light-lock";
    private static final String LOCK_NAME_SEPARATOR = ".";


    @Autowired
    private LockConfig lockConfig;

    @Autowired
    private BusinessKeyProvider businessKeyProvider;

    public LockInfo get(JoinPoint joinPoint, LightLock lock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockType type = lock.lockType();
        String businessKeyName = businessKeyProvider.getKeyName(joinPoint, lock);
        // 锁的名字，锁的粒度就是这里控制的
        String lockName = LOCK_NAME_PREFIX + LOCK_NAME_SEPARATOR + getName(lock.name(), signature) + businessKeyName;
        long waitTime = getWaitTime(lock);
        long leaseTime = getLeaseTime(lock);
        // 如果占用锁的时间设计不合理，则打印相应的警告提示
        if(leaseTime == -1 && log.isWarnEnabled()) {
            log.warn("Trying to acquire Lock({}) with no expiration, " +
                    "LightLock will keep prolong the lock expiration while the lock is still holding by current thread. " +
                    "This may cause dead lock in some circumstances.", lockName);
        }
        return new LockInfo(type, lockName, waitTime, leaseTime);
    }

    /**
     * 获取锁的name，如果没有指定，则按全类名拼接方法名处理
     */
    private String getName(String annotationName, MethodSignature signature) {
        if (annotationName.isEmpty()) {
            return String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName());
        } else {
            return annotationName;
        }
    }


    private long getWaitTime(LightLock lock) {
        return lock.waitTime() == Long.MIN_VALUE ?
                lockConfig.getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(LightLock lock) {
        return lock.leaseTime() == Long.MIN_VALUE ?
                lockConfig.getLeaseTime() : lock.leaseTime();
    }

}
