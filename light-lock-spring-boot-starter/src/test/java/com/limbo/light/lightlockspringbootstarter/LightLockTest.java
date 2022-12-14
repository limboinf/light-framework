package com.limbo.light.lightlockspringbootstarter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * test
 *
 * @author limbo
 * @since 2022/12/27 17:57
 */
@SpringBootTest(classes = LightLockSpringBootStarterApplicationTests.class)
public class LightLockTest {

    @Autowired
    private TestService testService;

    @Test
    @DisplayName("测试正常情况下")
    public void normalTest() {
        testService.getValue("jack", 100, 4);
    }

    /**
     * 测试
     * 成功获得锁
     * 锁自动过期
     */
    @Test
    @DisplayName("测试锁释放过期")
    public void autoReleaseTest() {
        testService.getValue("jack", 100, 10);
    }

    @DisplayName("测试并发情况下")
    @Test
    public void multithreadingTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        IntStream.range(0, 5).forEach(i -> {
            executorService.submit(() -> {
                testService.getValue("jack", 100, 4);
            });
        });

        executorService.awaitTermination(30, TimeUnit.SECONDS);

    }

    @Test
    public void lockTimeoutCustom() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        // 两个线程操作的key都一样, 模拟获得锁超时
        executorService.submit(() -> {
            testService.test2("limbo", 5);
            latch.countDown();
        });

        executorService.submit(() -> {
            testService.test2("limbo", 6);
            latch.countDown();
        });

        latch.await(12, TimeUnit.SECONDS);
    }

}
