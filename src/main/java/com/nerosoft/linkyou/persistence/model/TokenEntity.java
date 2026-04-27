package com.nerosoft.linkyou.persistence.model;

import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * TokenEntity类表示一个认证令牌的实体，包含令牌的基本信息和相关属性
 */
@Data
@Entity
@Table(name = "token")
public class TokenEntity implements Persistable<Long> {
    /**
     * Token ID，主键，雪花算法生成
     */
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Token类型，例如 "access_token"、"refresh_token" 等
     */
    @Column(name = "type", nullable = false, length = 36)
    private String type;

    /**
     * Token的Hash值，使用SHA-256算法生成，长度为256个字符
     */
    @Column(name = "key", nullable = false, length = 256)
    private String key;

    /**
     * 用户ID，关联到UserEntity的id字段，长度为36个字符
     */
    @Column(name = "subject", nullable = false, length = 36)
    private String subject;

    /**
     * 颁发时间，使用UTC时间，格式为ISO 8601，例如 "2024-01-01T00:00:00Z"
     */
    @Column(name = "issues", nullable = false)
    private LocalDateTime issues;

    /**
     * 过期时间，使用UTC时间，格式为ISO 8601，例如 "2024-12-31T23:59:59Z"
     */
    @Column(name = "expires", nullable = true)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.LOCAL_DATE_TIME)
    private Optional<LocalDateTime> expiresAt = Optional.empty();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return id == null || id <= 0;
    }
}
