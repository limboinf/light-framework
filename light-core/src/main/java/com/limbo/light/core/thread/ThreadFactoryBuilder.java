package com.limbo.light.core.thread;

import com.limbo.light.core.builder.BaseBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ThreadFactoryBuilder
 *
 * @author limbo
 * @since 2022/12/2 18:31
 */
public class ThreadFactoryBuilder implements BaseBuilder<ThreadFactory> {

    private static final long serialVersionUID = 1L;
    private ThreadFactory backingThreadFactory;
    private String namePrefix;
    private Boolean daemon;
    private Integer priority;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public ThreadFactoryBuilder() {
    }

    public static ThreadFactoryBuilder create() {
        return new ThreadFactoryBuilder();
    }

    public ThreadFactoryBuilder setThreadFactory(ThreadFactory backingThreadFactory) {
        this.backingThreadFactory = backingThreadFactory;
        return this;
    }

    public ThreadFactoryBuilder setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
        return this;
    }

    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public ThreadFactoryBuilder setPriority(int priority) {
        if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("Thread priority must be 1~10");
        }

        this.priority = priority;
        return this;
    }

    public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        return this;
    }

    @Override
    public ThreadFactory build() {
        return build(this);
    }

    private static ThreadFactory build(ThreadFactoryBuilder builder) {
        ThreadFactory backingThreadFactory = null != builder.backingThreadFactory ? builder.backingThreadFactory : Executors.defaultThreadFactory();
        String namePrefix = builder.namePrefix;
        Boolean daemon = builder.daemon;
        Integer priority = builder.priority;
        Thread.UncaughtExceptionHandler handler = builder.uncaughtExceptionHandler;
        AtomicLong count = null == namePrefix ? null : new AtomicLong();
        return (r) -> {
            Thread thread = backingThreadFactory.newThread(r);
            if (null != namePrefix) {
                thread.setName(namePrefix + count.getAndIncrement());
            }

            if (null != daemon) {
                thread.setDaemon(daemon);
            }

            if (null != priority) {
                thread.setPriority(priority);
            }

            if (null != handler) {
                thread.setUncaughtExceptionHandler(handler);
            }

            return thread;
        };
    }
}
