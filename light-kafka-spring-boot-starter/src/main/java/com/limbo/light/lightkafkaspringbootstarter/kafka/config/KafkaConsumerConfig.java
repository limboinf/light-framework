package com.limbo.light.lightkafkaspringbootstarter.kafka.config;

import com.limbo.light.lightkafkaspringbootstarter.kafka.KafkaListenerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.RetryingBatchErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * kafka 消费配置
 *
 * 设置批消费和单消费
 * 设置消息重试和死信队列
 * @author limbo 2022/12/6
 */
@Slf4j
public class KafkaConsumerConfig {

    private final KafkaConsumerServerProperties properties;
    private final KafkaTemplate<String, Object> template;

    public KafkaConsumerConfig(KafkaConsumerServerProperties properties, KafkaTemplate<String, Object> template) {
        this.properties = properties;
        this.template = template;
        log.info("LightKafka consumer properties: [{}]", properties);
    }

    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory () {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                KafkaListenerFactory.createFactory(true, false, properties);

        // 最大重试三次, 每隔2s, 期间会阻塞后续消费流程
        // 重试后仍然失败则塞入死信队列, topic名称为 {topic}.DLT ，注意需要提前手动创建该topic才行
        factory.setBatchErrorHandler(new RetryingBatchErrorHandler(
                new FixedBackOff(2000L, 3),
                new DeadLetterPublishingRecoverer(template)
                ));

        return factory;
    }

    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaSingleListenerContainerFactory() {

        final ConcurrentKafkaListenerContainerFactory<String, String> factory =
                KafkaListenerFactory.createFactory(false, false, properties);

        // 最大重试三次, 每隔2s, 期间会阻塞后续消费流程
        // 重试后仍然失败则塞入死信队列, topic名称为 {topic}.DLT ，注意需要提前手动创建该topic才行
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(template),
                new FixedBackOff(2000L, 3)));

        return factory;
    }
}
