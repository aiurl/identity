package com.nerosoft.linkyou.persistence.model;

import java.time.LocalDateTime;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * AuthlogEntity类表示一个认证日志的实体，包含认证日志的基本信息和相关属性
 */
@Entity
@Data
@Table(name = "authlog")
public class AuthlogEntity implements Persistable<Long> {
    /**
     * 认证日志ID，主键，雪花算法生成，确保全局唯一性和有序性
     */
    @Id
    private Long id;

    /**
     * 用户ID，关联到UserEntity的id字段，长度为36个字符
     */
    @Column(name = "user_id", nullable = true, length = 36)
    private String userId;

    /**
     * 用户名，长度为255个字符
     */
    @Column(name = "username", nullable = false, length = 255)
    private String username;

    /**
     * 认证结果，长度为32个字符，例如 "password"、"refresh_token" 等
     */
    @Column(name = "grant_type", nullable = false, length = 32)
    private String grantType;

    /**
     * 请求ID，长度为36个字符，用于追踪认证请求的来源和上下文
     */
    @Column(name = "request_id", nullable = true, length = 36)
    private String requestId;

    /**
     * IP地址，长度为15个字符，支持IPv4格式，例如 "114.114.114.114"
     */
    @Column(name = "ip_address", nullable = true, length = 15)
    private String ipAddress;

    /**
     * 用户代理，长度为512个字符，包含浏览器和操作系统信息
     */
    @Column(name = "user_agent", nullable = true, length = 512)
    private String userAgent;

    /**
     * 请求来源地址
     */
    @Column(name = "reffer", nullable = true, length = 255)
    private String reffer;

    /**
     * 应用名称，长度为50个字符，用于区分不同的客户端应用
     */
    @Column(name = "app_name", nullable = true, length = 50)
    private String appName;

    /**
     * 应用版本，长度为20个字符，用于记录客户端应用的版本信息
     */
    @Column(name = "app_version", nullable = true, length = 20)
    private String appVersion;

    @Column(name = "os_platform", nullable = true, length = 16)
    private String osPlatform;

    @Column(name = "source", nullable = true, length = 32)
    private String source;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Column(name = "remark", nullable = true, length = 500)
    private String remark;

    /**
     * 认证时间，使用UTC时间，格式为ISO 8601，例如 "2024-01-01T00:00:00Z"
     */
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
