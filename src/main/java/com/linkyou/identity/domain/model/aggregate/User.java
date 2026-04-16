package com.linkyou.identity.domain.model.aggregate;

import com.linkyou.identity.common.exception.DomainException;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.Email;
import com.linkyou.identity.domain.model.valueobject.UserId;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class User {

    private final UserId id;
    private final String username;
    private final String nickname;
    private String passwordHash;
    private String passwordSalt;
    private final String phone;
    private final Email email;
    private int accessFailedCount;
    private LocalDateTime lockoutEnd;
    private final LocalDateTime createdAt;
    private LocalDateTime passwordChangedTime;
    private boolean active;
    private final Set<Role> roles;

    private User(UserId id,
                 String username,
                 String nickname,
                 Email email,
                 String passwordHash,
                 String passwordSalt,
                 String phone,
                 int accessFailedCount,
                 LocalDateTime lockoutEnd,
                 LocalDateTime createdAt,
                 LocalDateTime passwordChangedTime,
                 boolean active,
                 Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.phone = phone;
        this.accessFailedCount = accessFailedCount;
        this.lockoutEnd = lockoutEnd;
        this.createdAt = createdAt;
        this.passwordChangedTime = passwordChangedTime;
        this.active = active;
        this.roles = roles;
    }

    public static User register(String username, String email) {
        return register(username, username, "", email, "", "");
    }

    public static User register(String username,
                                String nickname,
                                String phone,
                                String email,
                                String passwordHash,
                                String passwordSalt) {
        if (username == null || username.isBlank()) {
            throw new DomainException("Username cannot be blank");
        }
        LocalDateTime now = LocalDateTime.now();
        return new User(
                UserId.newId(),
                username.trim(),
                nickname == null || nickname.isBlank() ? username.trim() : nickname.trim(),
                new Email(email),
                passwordHash == null ? "" : passwordHash,
                passwordSalt == null ? "" : passwordSalt,
                phone == null ? "" : phone.trim(),
                0,
                null,
                now,
                now,
                true,
                new HashSet<>()
        );
    }

    public static User reconstitute(String id,
                                    String username,
                                    String nickname,
                                    String phone,
                                    String email,
                                    String passwordHash,
                                    String passwordSalt,
                                    int accessFailedCount,
                                    LocalDateTime lockoutEnd,
                                    LocalDateTime createdAt,
                                    LocalDateTime passwordChangedTime,
                                    boolean active,
                                    Set<Role> roles) {
        return new User(
                new UserId(id),
                username,
                nickname == null || nickname.isBlank() ? username : nickname,
                new Email(email),
                passwordHash == null ? "" : passwordHash,
                passwordSalt == null ? "" : passwordSalt,
                phone == null ? "" : phone,
                accessFailedCount,
                lockoutEnd,
                createdAt,
                passwordChangedTime,
                active,
                new HashSet<>(roles == null ? Set.of() : roles)
        );
    }

    public void assignRole(Role role) {
        if (role != null) {
            roles.add(role);
        }
    }

    public void markAccessFailed() {
        this.accessFailedCount++;
        if (this.accessFailedCount >= 5) {
            this.lockoutEnd = LocalDateTime.now().plusMinutes(15);
        }
    }

    public void resetAccessFailures() {
        this.accessFailedCount = 0;
        this.lockoutEnd = null;
    }

    public boolean isLocked() {
        return lockoutEnd != null && lockoutEnd.isAfter(LocalDateTime.now());
    }

    public void changePassword(String newPasswordHash, String newPasswordSalt) {
        if (newPasswordHash == null || newPasswordSalt == null) {
            throw new DomainException("Password hash and salt cannot be null");
        }
        this.passwordHash = newPasswordHash;
        this.passwordSalt = newPasswordSalt;
        this.passwordChangedTime = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public UserId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public int getAccessFailedCount() {
        return accessFailedCount;
    }

    public LocalDateTime getLockoutEnd() {
        return lockoutEnd;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPasswordChangedTime() {
        return passwordChangedTime;
    }

    public boolean isActive() {
        return active;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }
}
