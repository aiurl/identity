package com.nerosoft.linkyou.domain.aggregate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.nerosoft.linkyou.domain.event.UserPasswordChangedEvent;
import com.nerosoft.linkyou.domain.valueobject.EmailAddress;
import com.nerosoft.linkyou.domain.valueobject.PhoneNumber;
import com.nerosoft.linkyou.seedwork.Aggregate;
import com.nerosoft.linkyou.utility.Cryptography;
import com.nerosoft.linkyou.utility.RandomUtility;

/* 用户聚合根 */
public class User extends Aggregate<String> {
    private String username;
    private EmailAddress email;
    private PhoneNumber phone;
    private String nickname;
    private String avatarUrl;
    private String passwordHash;
    private String passwordSalt;
    private Optional<LocalDateTime> lockoutEnd = Optional.empty();
    private int accessFailedCount = 0;
    private Optional<LocalDateTime> passwordChangedAt = Optional.empty();

    private User(String id, String username) {
        setId(id);
        rename(username);
    }

    public static User create(String username) {
        return new User(UUID.randomUUID().toString(), username);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email == null ? null : email.value();
    }

    public String getPhone() {
        return phone == null ? null : phone.value();
    }

    public Optional<EmailAddress> getEmailAddress() {
        return Optional.ofNullable(email);
    }

    public Optional<PhoneNumber> getPhoneNumber() {
        return Optional.ofNullable(phone);
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public Optional<LocalDateTime> getLockoutEnd() {
        return lockoutEnd;
    }

    public int getAccessFailedCount() {
        return accessFailedCount;
    }

    public Optional<LocalDateTime> getPasswordChangedAt() {
        return passwordChangedAt;
    }

    public void rename(String username) {
        this.username = requireText(username, "username");
    }

    public void changeEmail(String email) {
        this.email = email == null || email.isBlank() ? null : EmailAddress.of(email);
    }

    public void changePhone(String phone) {
        this.phone = phone == null || phone.isBlank() ? null : PhoneNumber.of(phone);
    }

    public void updateProfile(String nickname, String avatarUrl) {
        this.nickname = normalizeOptionalText(nickname);
        this.avatarUrl = normalizeOptionalText(avatarUrl);
    }

    public void changePassword(String password, String changeType) throws Exception {
        String normalizedPassword = requireText(password, "password");
        String normalizedChangeType = normalizeChangeType(changeType);

        String salt = RandomUtility.randomString(32);
        String hash = Cryptography.AES.encrypt(normalizedPassword, salt);
        this.passwordSalt = salt;
        this.passwordHash = hash;

        if ("initial".equals(normalizedChangeType)) {
            this.passwordChangedAt = Optional.empty();
            return;
        }

        LocalDateTime changedAt = LocalDateTime.now();
        this.passwordChangedAt = Optional.of(changedAt);
        raiseEvent(new UserPasswordChangedEvent(getId(), normalizedChangeType, changedAt));
    }

    public void setPassword(String password, String changeType) throws Exception {
        changePassword(password, changeType);
    }

    public void recordAccessFailure() {
        this.accessFailedCount++;
    }

    public void resetAccessFailures() {
        this.accessFailedCount = 0;
    }

    public void lockUntil(LocalDateTime until) {
        if (until == null || !until.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Lockout time must be in the future");
        }
        this.lockoutEnd = Optional.of(until);
    }

    public void unlock() {
        this.lockoutEnd = Optional.empty();
        resetAccessFailures();
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return value.trim();
    }

    private String normalizeOptionalText(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String normalizeChangeType(String changeType) {
        String normalized = requireText(changeType, "changeType").toLowerCase();
        if (!normalized.equals("initial") && !normalized.equals("change") && !normalized.equals("reset")) {
            throw new IllegalArgumentException("Unsupported password change type");
        }
        return normalized;
    }
}
