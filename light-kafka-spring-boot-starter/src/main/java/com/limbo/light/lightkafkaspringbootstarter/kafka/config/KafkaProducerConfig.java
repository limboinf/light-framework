package com.limbo.light.lightkafkaspringbootstarter.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class KafkaProducerConfig {

    private final KafkaProducerServerProperties properties;

    public KafkaProducerConfig(KafkaProducerServerProperties properties) {
        this.properties = properties;
    }

    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> conf = new HashMap<>();
        conf.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getServers());
        conf.put(ProducerConfig.ACKS_CONFIG, properties.getAck());
        conf.put(ProducerConfig.CLIENT_ID_CONFIG, "LightKafkaService");
        conf.put(ProducerConfig.BATCH_SIZE_CONFIG, properties.getBatchSize());
        conf.put(ProducerConfig.LINGER_MS_CONFIG, properties.getLingerMs());
        conf.put(ProducerConfig.BUFFER_MEMORY_CONFIG, properties.getBufferMemory());
        conf.put(ProducerConfig.RETRIES_CONFIG, properties.getRetry());
        conf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        conf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.springframework.kafka.support.serializer.JsonSerializer");
        log.info("LightKafka - producer properties: [{}]", conf);
        return new DefaultKafkaProducerFactory<>(conf);
    }

    public KafkaTemplate<String, Object> kafkaTemplate() {
        ProducerFactory<String, Object> producerFactory = producerFactory();
        log.info("LightKafka - build kafkaTemplate named `lightKafkaTemplate`.");
        return new KafkaTemplate<>(producerFactory);
    }

}
