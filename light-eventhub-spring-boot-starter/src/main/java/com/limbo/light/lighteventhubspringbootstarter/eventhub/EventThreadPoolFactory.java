package com.limbo.light.lighteventhubspringbootstarter.eventhub;

import com.limbo.light.core.thread.MDCThreadPoolExecutor;
import com.limbo.light.core.thread.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class EventThreadPoolFactory {

    private static final int DEFAULT_CORE_SIZE = 0;
    private static final int DEFAULT_MAX_SIZE = 1;
    private static final int DEFAULT_TIMEOUT = 1;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.HOURS;
    private static final int DEFAULT_QUEUE_SIZE = 5000;
    private static final BlockingQueue<Runnable> DEFAULT_WORK_QUEUE = new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE);

    public static ExecutorService build(String identifier, Integer coreSize, Integer maxSize) {
        return new MDCThreadPoolExecutor(
                null == coreSize ? DEFAULT_CORE_SIZE : coreSize,
                null == maxSize ? DEFAULT_MAX_SIZE : maxSize,
                DEFAULT_TIMEOUT,
                DEFAULT_TIME_UNIT,
                DEFAULT_WORK_QUEUE,
                ThreadFactoryBuilder.create().setNamePrefix(String.format("EventHub-%s-", identifier)).build(),
                new BlockingPolicy()
                );
    }

}
