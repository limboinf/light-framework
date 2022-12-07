package com.limbo.light.lightkafkaspringbootstarter.kafka.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "light.kafka.consumer")
public class KafkaConsumerServerProperties extends BasicKafkaConsumerServerProperties {

}
