package com.limbo.light.lighteventhubspringbootstarter.eventhub;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;


/**
 * 事件总线
 *
 * @author limbo
 */
@Slf4j
@SuppressWarnings({"rawtypes"})
public class EventHub implements DisposableBean, ApplicationContextAware {

    private ApplicationContext ctx;
    private int coreSize = 0;
    private int maxPoolSize = 1;

    private final AtomicBoolean finished = new AtomicBoolean(false);
    private final Map<String, AsyncEventBus> eventBusMap = new ConcurrentHashMap<>();
    private final Map<String, ExecutorService> executorMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void onStart() {
        this.loadEventHandler();
    }

    public void loadEventHandler() {
        final Map<String, EventProcess> eventProcessMap = ctx.getBeansOfType(EventProcess.class);
        for (EventProcess process : eventProcessMap.values()) {
            this.register(process);
        }
    }

    private void register(EventProcess eventProcess) {

        final EventConsume eventConsume = eventProcess.getClass().getAnnotation(EventConsume.class);
        checkNotNull(eventConsume, "eventConsume不能为null");
        if (StringUtils.isBlank(eventConsume.identifier())) {
            log.error("EventProcess {} 没有设置identifier 忽略注册", eventConsume.getClass().getSimpleName());
            return;
        }

        final String identifier = eventConsume.identifier();
        AsyncEventBus asyncEventBus = eventBusMap.get(identifier);
        if (null == asyncEventBus) {
            final ExecutorService executor = EventThreadPoolFactory.build(identifier, coreSize, maxPoolSize);
            final AsyncEventBus eventBus = new AsyncEventBus(identifier, executor);
            asyncEventBus = ObjectUtils.defaultIfNull(eventBusMap.putIfAbsent(identifier, eventBus), eventBus);
            executorMap.put(identifier, executor);
        }
        asyncEventBus.register(eventProcess);
        log.info("EventHub 已注册 identifier: {}, eventProcess: {}", identifier, eventProcess.getClass().getName());
    }

    public EventBus getEventBus(String identifier) {
        checkState(!finished.get(), "eventbus is shutdown!");
        return eventBusMap.get(identifier);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
        Environment environment = applicationContext.getEnvironment();
        Integer corePoolSize = environment.getProperty("eventbus.async.executor.pool.size.core", Integer.class);
        if(corePoolSize != null){
            this.coreSize = corePoolSize;
        }
        Integer maxPoolSizeEvn = environment.getProperty("eventbus.async.executor.pool.size.max", Integer.class);
        if(maxPoolSizeEvn != null){
            this.maxPoolSize = maxPoolSizeEvn;
        }
    }

    @Override
    public void destroy() {
        log.info("销毁EventHub bean, 清理工作");
        finished.getAndSet(true);
        try {
            executorMap.forEach((s, executor) -> {
                executor.shutdown();
                try {
                    executor.awaitTermination(3, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    log.error("executor.awaitTermination 错误，中断线程！", e);
                    Thread.currentThread().interrupt();
                }
            });
        } catch (Exception e) {
            log.error("销毁EventHub bean 异常", e);
        }
    }
}
