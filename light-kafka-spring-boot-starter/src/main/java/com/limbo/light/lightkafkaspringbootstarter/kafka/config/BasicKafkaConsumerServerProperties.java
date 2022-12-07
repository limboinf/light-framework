package com.limbo.light.lightkafkaspringbootstarter.kafka.config;

import lombok.Data;


/**
 * 基本kafka消费者属性
 *
 * @author limbo
 * @since  2022/12/06
 */
@Data
public class BasicKafkaConsumerServerProperties {

    private String servers;

    private String groupId;

    private Integer sessionTimeoutMs = 30_000;

    private Integer heartbeatIntervalMs = 10_000;

    private Integer maxPoolRecords = 2000;

    private Integer concurrency = 2;

}
