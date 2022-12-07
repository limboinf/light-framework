package com.limbo.light.lightkafkaspringbootstarter.kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * kafka 抽象消费者
 *
 * @param <T> topic对应的数据实体
 * @param <H> 实现 {@link ConsumeMessageHandler} 的实体消息处理者
 *
 * @author limbo
 * @since 2022/12/6 11:52
 */
@Slf4j
public class AbstractMessageConsumer<T, H extends ConsumeMessageHandler<T>> {

    @Resource
    private ApplicationContext context;

    /**
     *  topic 处理者集合
     */
    private Collection<H> handlers;

    /**
     * 通过构造器参数配置 可选择是否进行异步处理，不要过度异步化，对于耗时的消费逻辑建议使用
     */
    private final Executor executor;

    private final Class<H> handleType;
    private final String className;
    private final Class<T> dtoClz;

    protected AbstractMessageConsumer() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public AbstractMessageConsumer(Executor executor) {
        this.executor = executor;
        this.className = getClass().getSimpleName();
        Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.handleType = (Class<H>) actualTypeArguments[1];
        this.dtoClz = (Class<T>) actualTypeArguments[0];
    }

    protected void onReceive(T message) {
        log.debug("{} receive msg: {}", className, message);
        getHandlers().forEach(handler -> {
            try {
                if (executor != null) {
                    executor.execute(() -> handler.process(message));
                } else {
                    handler.process(message);
                }
            } catch (Exception e) {
                log.error("{} 事件处理异常: {}", className, message, e);
            }
        });
        log.debug("{} handle msg: {}", className, message);

    }

    protected void onReceive(String message) {
        log.debug("{} receive msg: {}", className, message);
        try {
            T msg = JSON.parseObject(message, dtoClz);
            onReceive(msg);
        } catch (Exception e) {
            log.error("{} 消息解析 type: {} 失败: {}", className, dtoClz.getSimpleName(), message, e);
        } finally {
            log.debug("{} handle msg: {}", className, message);
        }
    }

    protected void onReceiveBatchRecord(List<T> messageList) {
        getHandlers().forEach(handler -> {
            try {
                if (executor != null) {
                    executor.execute(() -> handler.processBatch(messageList));
                } else {
                    handler.processBatch(messageList);
                }
            } catch (Exception e) {
                log.error("{} 事件处理异常: {}", className, messageList, e);
            }
        });

    }

    protected void onReceiveBatch(List<ConsumerRecord<String, String>> messageList) {
        List<T> records = new ArrayList<>();
        messageList.forEach(message -> {
            try {
                T msg = JSON.parseObject(message.value(), dtoClz);
                records.add(msg);
            } catch (Exception e) {
                log.error("kafka consumer onReceiveBatch json parse error, topic: [{}], partition: [{}], offset: [{}], msg: [{}]",
                        message.topic(), message.partition(), message.offset(), message.value(), e);
            }
        });

        onReceiveBatchRecord(records);
    }

    private Collection<H> getHandlers() {
        if (CollectionUtils.isEmpty(handlers)) {
            handlers = context.getBeansOfType(handleType).values();
        }
        return handlers;
    }
}
