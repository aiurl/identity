package com.nerosoft.linkyou.domain.event;

import java.time.LocalDateTime;

import com.nerosoft.linkyou.seedwork.DomainEvent;

/**
 * 用户密码变更事件
 */
public final class UserPasswordChangedEvent extends DomainEvent {
    private final String userId;
    private final String changeType;
    private final LocalDateTime changedAt;

    /**
     * 构造函数
     * 
     * @param userId     发生密码变更的用户ID
     * @param changeType 密码变更类型，例如 "change" 、 "reset" 或 ”initial“
     * @param changedAt  密码变更发生的时间
     */
    public UserPasswordChangedEvent(String userId, String changeType, LocalDateTime changedAt) {
        this.userId = userId;
        this.changeType = changeType;
        this.changedAt = changedAt;
    }

    /**
     * 获取发生密码变更的用户ID
     * @return 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 获取密码变更类型
     * @return 密码变更类型，例如 "change" 、 "reset" 或 ”initial“
     */
    public String getChangeType() {
        return changeType;
    }

    /**
     * 获取密码变更发生的时间
     * @return 密码变更发生的时间
     */
    public LocalDateTime getChangedAt() {
        return changedAt;
    }
}
