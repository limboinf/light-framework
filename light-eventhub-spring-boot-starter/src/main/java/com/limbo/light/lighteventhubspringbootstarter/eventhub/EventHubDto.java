package com.limbo.light.lighteventhubspringbootstarter.eventhub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * databus 消息体
 *
 * @author limbo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventHubDto<T> {

    /**
     * 消息唯一id
     */
    private String mid;

    /**
     * 消息时间戳
     */
    private long ts;

    /**
     * 消息体
     */
    private List<T> records;

    public static <T> EventHubDto<T> build(@NonNull List<T> items) {
        EventHubDto<T> dto = new EventHubDto<>();
        dto.setMid(UUID.randomUUID().toString());
        dto.setTs(System.currentTimeMillis());
        dto.setRecords(items);
        return dto;
    }
}
