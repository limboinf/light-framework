package com.limbo.light.log.interceptor;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * spring异步线程获得主线程的上下文,获取主线程traceId
 *
 * @author limbo
 * @since 2022/12/1 20:24
 */
public class AsyncMDCTaskDecorator implements TaskDecorator {

    /**
     * <p>
     * decorate 方法复写
     * </p>
     *
     * sl4j的MDC机制内部基于ThreadLocal实现，当MDC.put()方法调用是只会对当前线程生效
     * 所以在主线程设置MDC数据，其子线程（线程池）无法获取到，需要在创建子线程前及将MDC数据传递给子线程
     *
     * @param runnable 可运行
     * @return {@link Runnable}
     */
    @Override
    public Runnable decorate(@Nullable Runnable runnable) {
        assert runnable != null;
        // 在父线程新建子线程之前调用MDC.getCopyOfContextMap()方法将MDC内容取出来传给子线程
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (contextMap != null) {
                    // 子线程在执行操作前先调用MDC.setContextMap()方法将父线程的MDC内容设置到子线程
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

}
