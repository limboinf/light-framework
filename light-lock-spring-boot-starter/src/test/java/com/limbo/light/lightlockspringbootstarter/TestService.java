package com.limbo.light.lightlockspringbootstarter;

import com.limbo.light.lightlockspringbootstarter.annotation.LightLock;
import com.limbo.light.lightlockspringbootstarter.annotation.LockKey;
import com.limbo.light.lightlockspringbootstarter.core.ReleaseTimeoutStrategy;
import com.limbo.light.lightlockspringbootstarter.domain.LockType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * test service
 *
 * @author limbo
 * @since 2022/12/27 17:51
 */
@Slf4j
@Service
public class TestService {

    @LightLock(
            name = "testLock",
            lockType = LockType.Reentrant,
            waitTime = 5,
            leaseTime = 10,
            keys = {"#userName"},
            releaseTimeoutStrategy = ReleaseTimeoutStrategy.FAIL_FAST)
    public String getValue(String userName, @LockKey Integer id, int sleepTime) {
        log.info("userName: {} id: {} 业务逻辑处理...", userName, id);
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("userName: {} id: {} 业务逻辑完成", userName, id);
        return "OK";
    }

    @LightLock(
            name = "test2Lock",
            waitTime = 5,
            leaseTime = 10,
            customLockTimeoutStrategy = "customLockTimeout",
            releaseTimeoutStrategy = ReleaseTimeoutStrategy.FAIL_FAST)
    public String test2(String userName, int sleepTime) {
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    private String customLockTimeout(String userName, int sleepTime) {
        log.info("自定义获取锁超时处理策略，方法签名要与被注解标记的方法一致");
        log.info("userName: {} sleepTime: {}", userName, sleepTime);
        return "No";
    }

}
