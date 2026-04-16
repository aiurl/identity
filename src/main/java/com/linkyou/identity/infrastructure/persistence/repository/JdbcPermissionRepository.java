package com.linkyou.identity.infrastructure.persistence.repository;

import com.linkyou.identity.domain.model.entity.Permission;
import com.linkyou.identity.domain.model.valueobject.PermissionId;
import com.linkyou.identity.domain.repository.PermissionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnExpression("'${identity.repository.type:inmemory}' == 'jdbc' || '${identity.repository.type:inmemory}' == 'mysql' || '${identity.repository.type:inmemory}' == 'postgresql'")
public class JdbcPermissionRepository implements PermissionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPermissionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Permission save(Permission permission) {
        String permissionId = findIdByCode(permission.getCode()).orElse(permission.getId().value());
        if (existsById(permissionId)) {
            jdbcTemplate.update(
                    "update sys_permissions set code = ?, description = ? where id = ?",
                    permission.getCode(),
                    permission.getDescription(),
                    permissionId
            );
        } else {
            jdbcTemplate.update(
                    "insert into sys_permissions (id, code, description) values (?, ?, ?)",
                    permissionId,
                    permission.getCode(),
                    permission.getDescription()
            );
        }
        return findById(new PermissionId(permissionId)).orElse(permission);
    }

    @Override
    public Optional<Permission> findById(PermissionId id) {
        List<Permission> permissions = jdbcTemplate.query(
                "select id, code, description from sys_permissions where id = ?",
                (rs, rowNum) -> Permission.reconstitute(
                        rs.getString("id"),
                        rs.getString("code"),
                        rs.getString("description")
                ),
                id.value()
        );
        return permissions.stream().findFirst();
    }

    @Override
    public List<Permission> findAll() {
        return jdbcTemplate.query(
                "select id, code, description from sys_permissions order by code",
                (rs, rowNum) -> Permission.reconstitute(
                        rs.getString("id"),
                        rs.getString("code"),
                        rs.getString("description")
                )
        );
    }

    protected Optional<String> findIdByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        List<String> ids = jdbcTemplate.query(
                "select id from sys_permissions where lower(code) = lower(?)",
                (rs, rowNum) -> rs.getString("id"),
                code
        );
        return ids.stream().findFirst();
    }

    private boolean existsById(String id) {
        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from sys_permissions where id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }
}
