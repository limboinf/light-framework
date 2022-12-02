package com.limbo.light.lighteventhubspringbootstarter.eventhub;

import com.google.common.eventbus.Subscribe;

/**
 * 事件总线-消费接口
 *
 * @author limbo
 */
public interface EventProcess<T> {

    @Subscribe
    void process(EventHubDto<T> eventHubDto);

}
