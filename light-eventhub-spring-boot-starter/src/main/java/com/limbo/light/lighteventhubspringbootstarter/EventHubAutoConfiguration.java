package com.limbo.light.lighteventhubspringbootstarter;

import com.limbo.light.lighteventhubspringbootstarter.eventhub.EventHub;
import com.limbo.light.lighteventhubspringbootstarter.eventhub.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author limbo
 * @since 2022/12/2 18:37
 */
@Configuration
@ConditionalOnMissingBean(EventProducer.class)
public class EventHubAutoConfiguration {

    @Bean
    public EventHub eventHub() {
        return new EventHub();
    }

    @Bean
    public EventProducer eventProducer (@Autowired EventHub eventHub) {
        return new EventProducer(eventHub);
    }

}
