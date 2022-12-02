package com.limbo.light.core.thread;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

/**
 * description
 *
 * @author limbo
 * @since 2022/12/2 21:18
 */
public class MDCThreadPoolExecutor extends ThreadPoolExecutor {

    public MDCThreadPoolExecutor(int corePoolSize,
                                 int maximumPoolSize,
                                 long keepAliveTime,
                                 TimeUnit unit,
                                 BlockingQueue<Runnable> workQueue,
                                 ThreadFactory threadFactory,
                                 RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(final Runnable runnable) {
        // 在异步线程执行前获取父线程MDC上下午
        final Map<String, String> context = MDC.getCopyOfContextMap();
        super.execute(() -> {
            // 父线程的MDC上下午 传子线程
            MDC.setContextMap(context);
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        });
    }
}
