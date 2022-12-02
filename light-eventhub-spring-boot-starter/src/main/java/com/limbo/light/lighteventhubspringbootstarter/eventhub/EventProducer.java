package com.limbo.light.lighteventhubspringbootstarter.eventhub;

import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 事件总线-生产者
 *
 * @author limbo
 * @date 2022/04/07
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings({"UnstableApiUsage"})
public class EventProducer {

    private final EventHub eventHub;

    public <T> void post(String identifier, final T data) {
        post(identifier, List.of(data));
    }

    public <T> void post(String identifier, final List<T> list) {
        final EventBus eventBus = eventHub.getEventBus(identifier);
        if (eventBus == null) {
            log.error("identifier {} 没有事件监听者", identifier);
            return;
        }

        EventHubDto<T> hubDto = EventHubDto.build(list);
        eventBus.post(hubDto);
        log.info("事件总线: identifier: {} 发布事件成功, hubDto: {}", identifier, hubDto);
    }
}
