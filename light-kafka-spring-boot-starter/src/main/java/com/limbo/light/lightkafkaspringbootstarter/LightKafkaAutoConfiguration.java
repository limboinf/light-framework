package com.limbo.light.lightkafkaspringbootstarter;

import com.limbo.light.lightkafkaspringbootstarter.kafka.config.KafkaConsumerConfig;
import com.limbo.light.lightkafkaspringbootstarter.kafka.config.KafkaConsumerServerProperties;
import com.limbo.light.lightkafkaspringbootstarter.kafka.config.KafkaProducerConfig;
import com.limbo.light.lightkafkaspringbootstarter.kafka.config.KafkaProducerServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * kafka 自动配置类
 *
 * @author limbo
 * @since 2022/12/6 11:52
 */
@Slf4j
@SuppressWarnings("rawtypes")
@Configuration
@EnableConfigurationProperties(value = {KafkaConsumerServerProperties.class, KafkaProducerServerProperties.class})
@ConditionalOnProperty(prefix = "light.kafka", value = "enabled", havingValue = "true", matchIfMissing = true)
public class LightKafkaAutoConfiguration {

    @Autowired
    private KafkaProducerServerProperties kafkaProducerServerProperties;

    @Autowired
    private KafkaConsumerServerProperties kafkaConsumerServerProperties;

    @Bean(name = "lightKafkaTemplate")
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaProducerConfig(kafkaProducerServerProperties).kafkaTemplate();
    }

    @Bean(name = "batchKafkaContainer")
    @ConditionalOnBean(name = "lightKafkaTemplate")
    public KafkaListenerContainerFactory kafkaBatchListenerContainerFactory(
            @Qualifier("lightKafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate) {

        log.info("LightKafka build batch kafkaListenerContainerFactory named `batchKafkaContainer`");
        return new KafkaConsumerConfig(kafkaConsumerServerProperties, kafkaTemplate).kafkaListenerContainerFactory();
    }

    @Bean(name = "singleKafkaContainer")
    @ConditionalOnBean(name = "lightKafkaTemplate")
    public KafkaListenerContainerFactory kafkaSingleListenerContainerFactory(
            @Qualifier("lightKafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate) {

        log.info("LightKafka build batch kafkaListenerContainerFactory named `singleKafkaContainer`");
        return new KafkaConsumerConfig(kafkaConsumerServerProperties, kafkaTemplate).kafkaSingleListenerContainerFactory();
    }

}
