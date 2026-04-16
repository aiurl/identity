package com.linkyou.identity.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sys_users")
public class UserEntity extends AuditableEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(nullable = false, unique = true, length = 128)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "password_salt", nullable = false, length = 255)
    private String passwordSalt;

    @Column(length = 32, unique = true)
    private String phone;

    @Column(name = "access_failed_count", nullable = false)
    private int accessFailedCount;

    @Column(name = "lockout_end")
    private LocalDateTime lockoutEnd;

    @Column(name = "password_changed_time", nullable = false)
    private LocalDateTime passwordChangedTime;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    protected UserEntity() {
    }

    @PrePersist
    protected void initializeUserAuditFields() {
        if (nickname == null || nickname.isBlank()) {
            nickname = username;
        }
        if (passwordChangedTime == null) {
            passwordChangedTime = LocalDateTime.now();
        }
    }
}
