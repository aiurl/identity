package com.nerosoft.linkyou.persistence.model;

import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.annotations.JdbcTypeCode;

import com.nerosoft.linkyou.seedwork.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * UserEntity类表示一个用户的实体，包含用户的基本信息和相关属性
 */
@Entity
@Table(name = "users")
public class UserEntity implements Persistable<String> {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "password_salt", nullable = false, length = 255)
    private String passwordSalt;

    @Column(name = "email", length = 100)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.VARCHAR)
    private Optional<String> email = Optional.empty();

    @Column(name = "phone", length = 20)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.VARCHAR)
    private Optional<String> phone = Optional.empty();

    @Column(name = "nickname", length = 50)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.VARCHAR)
    private Optional<String> nickname = Optional.empty();

    @Column(name = "avatar_url", length = 255)
    @JdbcTypeCode(org.hibernate.type.SqlTypes.VARCHAR)
    private Optional<String> avatarUrl = Optional.empty();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "access_failed_count", nullable = false)
    private int accessFailedCount;

    @Column(name = "lockout_end")
    @JdbcTypeCode(org.hibernate.type.SqlTypes.LOCAL_DATE_TIME)
    private Optional<LocalDateTime> lockoutEnd = Optional.empty();

    @Column(name = "password_changed_at")
    @JdbcTypeCode(org.hibernate.type.SqlTypes.LOCAL_DATE_TIME)
    private Optional<LocalDateTime> passwordChangedAt = Optional.empty();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}

