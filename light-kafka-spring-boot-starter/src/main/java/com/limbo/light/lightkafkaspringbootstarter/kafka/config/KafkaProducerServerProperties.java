package com.limbo.light.lightkafkaspringbootstarter.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * kafka生产者配置
 *
 * @author limbo
 * @date 2022/11/10
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "light.kafka.producer")
public class KafkaProducerServerProperties {

    private String servers;

    private String ack = "1";

    private String batchSize = "819200";

    private String lingerMs = "100";

    private String retry = "3";

    private String bufferMemory = "33554432";
}
