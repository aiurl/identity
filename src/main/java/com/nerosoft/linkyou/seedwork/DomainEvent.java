package com.nerosoft.linkyou.seedwork;

/**
 * 领域事件基类
 * 
 * 所有领域事件都应该继承自这个基类，以便于统一管理和处理。
 * 
 * @author nerosoft
 * @version 1.0
 */
public abstract class DomainEvent {
    private final String eventId = java.util.UUID.randomUUID().toString();
    private final java.time.Instant occurredAt = java.time.Instant.now();

    /**
     * 获取事件的唯一标识
     * @return 事件的唯一标识
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * 获取事件发生的时间
     * @return 事件发生的时间
     */
    public java.time.Instant getOccurredAt() {
        return occurredAt;
    }
}
