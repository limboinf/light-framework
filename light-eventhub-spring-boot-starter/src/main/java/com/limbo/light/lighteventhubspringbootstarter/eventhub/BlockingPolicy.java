package com.limbo.light.lighteventhubspringbootstarter.eventhub;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class BlockingPolicy implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.error("线程池 {} 等待队列已满，正在执行阻塞等待", executor.toString());
        if (!executor.isShutdown()) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                log.error("线程池 {} 阻塞策略异常", executor, e);
            }
        }
    }
}
