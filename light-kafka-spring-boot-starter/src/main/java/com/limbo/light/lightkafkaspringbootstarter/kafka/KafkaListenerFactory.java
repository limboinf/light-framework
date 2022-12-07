package com.limbo.light.lightkafkaspringbootstarter.kafka;

import com.limbo.light.lightkafkaspringbootstarter.kafka.config.BasicKafkaConsumerServerProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * KafkaListenerFactory
 *
 * @author limbo
 * @since 2022/12/6 11:52
 */
public class KafkaListenerFactory {

    private KafkaListenerFactory() {
    }

    /**
     * 创建工厂
     *
     * @param batch      是否批处理
     * @param autoCommit 是否自动提交
     * @param properties 消费属性
     * @return {@link ConcurrentKafkaListenerContainerFactory}<{@link String}, {@link String}>
     */
    public static ConcurrentKafkaListenerContainerFactory<String, String> createFactory(
            boolean batch, boolean autoCommit, BasicKafkaConsumerServerProperties properties) {

        final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig(autoCommit, properties)));
        factory.setConcurrency(properties.getConcurrency());
        if (batch) {
            factory.setBatchListener(true);
            factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
            factory.setMessageConverter(new BatchMessagingMessageConverter(new StringJsonMessageConverter()));
        } else {
            factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
            factory.setMessageConverter(new StringJsonMessageConverter());
        }

        factory.getContainerProperties().setPollTimeout(6000L);
        return factory;
    }

    /**
     * 消费者的配置
     *
     * @param autoCommit 自动提交
     * @param properties 消费属性
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    private static Map<String, Object> consumerConfig(boolean autoCommit, BasicKafkaConsumerServerProperties properties) {
        Map<String, Object> props = new HashMap<>(16);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());

        // 如果自动提交(默认 true）, 需要配合客户端参数 auto.commit.interval.ms（默认5s）定期提交
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 3000);

        // 拉取批量消息条数，默认500条
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, properties.getMaxPoolRecords());

        // session.timeout.ms 指定消费者在多久内没有发送心跳给GroupCoordinator 认为 Dead，默认5s
        // Dead则会触发再均衡，把它的分区分配给其他消费者
        // 该属性与 heartbeat.interval.ms 紧密相关
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, properties.getSessionTimeoutMs());
        // heartbeat.interval.ms 指定了 poll() 方法向协调器发送心跳的频率，session.timeout.ms 指定了消费者最长多久不发送心跳
        // 一般是 session.timeout.ms 的三分之一
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, properties.getHeartbeatIntervalMs());

        // K-V序列化
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // 默认 30000
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 90000);
        return props;
    }
}
