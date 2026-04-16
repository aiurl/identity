package com.linkyou.identity.infrastructure.persistence.repository;

import com.linkyou.identity.domain.model.aggregate.User;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.UserId;
import com.linkyou.identity.domain.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@ConditionalOnExpression("'${identity.repository.type:inmemory}' == 'jdbc' || '${identity.repository.type:inmemory}' == 'mysql' || '${identity.repository.type:inmemory}' == 'postgresql'")
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {
        if (existsById(user.getId().value())) {
            jdbcTemplate.update(
                    "update sys_users set username = ?, nickname = ?, password_hash = ?, password_salt = ?, phone = ?, email = ?, access_failed_count = ?, lockout_end = ?, is_active = ?, password_changed_time = ? where id = ?",
                    user.getUsername(),
                    user.getNickname(),
                    user.getPasswordHash(),
                    user.getPasswordSalt(),
                    emptyToNull(user.getPhone()),
                    user.getEmail().value(),
                    user.getAccessFailedCount(),
                    toTimestamp(user.getLockoutEnd()),
                    user.isActive(),
                    toTimestamp(user.getPasswordChangedTime()),
                    user.getId().value()
            );
        } else {
            jdbcTemplate.update(
                    "insert into sys_users (id, username, nickname, password_hash, password_salt, phone, email, access_failed_count, lockout_end, is_active, created_at, password_changed_time, updated_at) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    user.getId().value(),
                    user.getUsername(),
                    user.getNickname(),
                    user.getPasswordHash(),
                    user.getPasswordSalt(),
                    emptyToNull(user.getPhone()),
                    user.getEmail().value(),
                    user.getAccessFailedCount(),
                    toTimestamp(user.getLockoutEnd()),
                    user.isActive(),
                    toTimestamp(user.getCreatedAt()),
                    toTimestamp(user.getPasswordChangedTime()),
                    Timestamp.valueOf(LocalDateTime.now())
            );
        }

        jdbcTemplate.update("delete from sys_user_roles where user_id = ?", user.getId().value());
        for (Role role : user.getRoles()) {
            String roleId = saveRoleReference(role);
            jdbcTemplate.update(
                    "insert into sys_user_roles (user_id, role_id) values (?, ?)",
                    user.getId().value(),
                    roleId
            );
        }

        return findById(user.getId()).orElse(user);
    }

    @Override
    public Optional<User> findById(UserId id) {
        List<User> users = jdbcTemplate.query(
                "select id, username, nickname, password_hash, password_salt, phone, email, access_failed_count, lockout_end, is_active, created_at, password_changed_time from sys_users where id = ?",
                (rs, rowNum) -> mapUser(rs.getString("id"), rs.getString("username"), rs.getString("nickname"), rs.getString("phone"), rs.getString("email"), rs.getString("password_hash"), rs.getString("password_salt"), rs.getInt("access_failed_count"), toLocalDateTime(rs.getTimestamp("lockout_end")), toLocalDateTime(rs.getTimestamp("created_at")), toLocalDateTime(rs.getTimestamp("password_changed_time")), rs.getBoolean("is_active")),
                id.value()
        );
        return users.stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }
        List<User> users = jdbcTemplate.query(
                "select id, username, nickname, password_hash, password_salt, phone, email, access_failed_count, lockout_end, is_active, created_at, password_changed_time from sys_users where lower(username) = lower(?)",
                (rs, rowNum) -> mapUser(rs.getString("id"), rs.getString("username"), rs.getString("nickname"), rs.getString("phone"), rs.getString("email"), rs.getString("password_hash"), rs.getString("password_salt"), rs.getInt("access_failed_count"), toLocalDateTime(rs.getTimestamp("lockout_end")), toLocalDateTime(rs.getTimestamp("created_at")), toLocalDateTime(rs.getTimestamp("password_changed_time")), rs.getBoolean("is_active")),
                username
        );
        return users.stream().findFirst();
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return Optional.empty();
        }
        List<User> users = jdbcTemplate.query(
                "select id, username, nickname, password_hash, password_salt, phone, email, access_failed_count, lockout_end, is_active, created_at, password_changed_time from sys_users where lower(phone) = lower(?)",
                (rs, rowNum) -> mapUser(rs.getString("id"), rs.getString("username"), rs.getString("nickname"), rs.getString("phone"), rs.getString("email"), rs.getString("password_hash"), rs.getString("password_salt"), rs.getInt("access_failed_count"), toLocalDateTime(rs.getTimestamp("lockout_end")), toLocalDateTime(rs.getTimestamp("created_at")), toLocalDateTime(rs.getTimestamp("password_changed_time")), rs.getBoolean("is_active")),
                phone
        );
        return users.stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        List<User> users = jdbcTemplate.query(
                "select id, username, nickname, password_hash, password_salt, phone, email, access_failed_count, lockout_end, is_active, created_at, password_changed_time from sys_users where lower(email) = lower(?)",
                (rs, rowNum) -> mapUser(rs.getString("id"), rs.getString("username"), rs.getString("nickname"), rs.getString("phone"), rs.getString("email"), rs.getString("password_hash"), rs.getString("password_salt"), rs.getInt("access_failed_count"), toLocalDateTime(rs.getTimestamp("lockout_end")), toLocalDateTime(rs.getTimestamp("created_at")), toLocalDateTime(rs.getTimestamp("password_changed_time")), rs.getBoolean("is_active")),
                email
        );
        return users.stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                "select id, username, nickname, password_hash, password_salt, phone, email, access_failed_count, lockout_end, is_active, created_at, password_changed_time from sys_users order by created_at, username",
                (rs, rowNum) -> mapUser(rs.getString("id"), rs.getString("username"), rs.getString("nickname"), rs.getString("phone"), rs.getString("email"), rs.getString("password_hash"), rs.getString("password_salt"), rs.getInt("access_failed_count"), toLocalDateTime(rs.getTimestamp("lockout_end")), toLocalDateTime(rs.getTimestamp("created_at")), toLocalDateTime(rs.getTimestamp("password_changed_time")), rs.getBoolean("is_active"))
        );
    }

    private User mapUser(String id, String username, String nickname, String phone, String email, String passwordHash, String passwordSalt,
                         int accessFailedCount, LocalDateTime lockoutEnd, LocalDateTime createdAt, LocalDateTime passwordChangedTime,
                         boolean active) {
        return User.reconstitute(
                id,
                username,
                nickname,
                phone,
                email,
                passwordHash,
                passwordSalt,
                accessFailedCount,
                lockoutEnd,
                createdAt,
                passwordChangedTime,
                active,
                findRolesByUserId(id)
        );
    }

    private Set<Role> findRolesByUserId(String userId) {
        return new LinkedHashSet<>(jdbcTemplate.query(
                "select r.id, r.name, r.description from sys_roles r join sys_user_roles ur on ur.role_id = r.id where ur.user_id = ? order by r.name",
                (rs, rowNum) -> Role.reconstitute(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        Set.of()
                ),
                userId
        ));
    }

    private String saveRoleReference(Role role) {
        Optional<String> existingId = findRoleIdByName(role.getName());
        String roleId = existingId.orElse(role.getId().value());
        if (existsRoleById(roleId)) {
            jdbcTemplate.update(
                    "update sys_roles set name = ?, description = ? where id = ?",
                    role.getName(),
                    role.getDescription(),
                    roleId
            );
        } else {
            jdbcTemplate.update(
                    "insert into sys_roles (id, name, description) values (?, ?, ?)",
                    roleId,
                    role.getName(),
                    role.getDescription()
            );
        }
        return roleId;
    }

    private Optional<String> findRoleIdByName(String name) {
        List<String> ids = jdbcTemplate.query(
                "select id from sys_roles where lower(name) = lower(?)",
                (rs, rowNum) -> rs.getString("id"),
                name
        );
        return ids.stream().findFirst();
    }

    private boolean existsById(String id) {
        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from sys_users where id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    private boolean existsRoleById(String id) {
        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from sys_roles where id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    private Timestamp toTimestamp(LocalDateTime value) {
        return value == null ? null : Timestamp.valueOf(value);
    }

    private LocalDateTime toLocalDateTime(Timestamp value) {
        return value == null ? null : value.toLocalDateTime();
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
