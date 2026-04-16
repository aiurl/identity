package com.linkyou.identity.infrastructure.persistence.repository;

import com.linkyou.identity.domain.model.entity.Permission;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.RoleId;
import com.linkyou.identity.domain.repository.RoleRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@ConditionalOnExpression("'${identity.repository.type:inmemory}' == 'jdbc' || '${identity.repository.type:inmemory}' == 'mysql' || '${identity.repository.type:inmemory}' == 'postgresql'")
public class JdbcRoleRepository implements RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcRoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Role save(Role role) {
        String roleId = findIdByName(role.getName()).orElse(role.getId().value());
        if (existsById(roleId)) {
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

        jdbcTemplate.update("delete from sys_role_permissions where role_id = ?", roleId);
        for (Permission permission : role.getPermissions()) {
            String permissionId = savePermissionReference(permission);
            jdbcTemplate.update(
                    "insert into sys_role_permissions (role_id, permission_id) values (?, ?)",
                    roleId,
                    permissionId
            );
        }

        return findById(new RoleId(roleId)).orElse(role);
    }

    @Override
    public Optional<Role> findById(RoleId id) {
        List<Role> roles = jdbcTemplate.query(
                "select id, name, description from sys_roles where id = ?",
                (rs, rowNum) -> Role.reconstitute(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        findPermissionsByRoleId(rs.getString("id"))
                ),
                id.value()
        );
        return roles.stream().findFirst();
    }

    @Override
    public List<Role> findAll() {
        return jdbcTemplate.query(
                "select id, name, description from sys_roles order by name",
                (rs, rowNum) -> Role.reconstitute(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        findPermissionsByRoleId(rs.getString("id"))
                )
        );
    }

    protected Optional<String> findIdByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        List<String> ids = jdbcTemplate.query(
                "select id from sys_roles where lower(name) = lower(?)",
                (rs, rowNum) -> rs.getString("id"),
                name
        );
        return ids.stream().findFirst();
    }

    protected Set<Permission> findPermissionsByRoleId(String roleId) {
        return new LinkedHashSet<>(jdbcTemplate.query(
                "select p.id, p.code, p.description from sys_permissions p " +
                        "join sys_role_permissions rp on rp.permission_id = p.id where rp.role_id = ? order by p.code",
                (rs, rowNum) -> Permission.reconstitute(
                        rs.getString("id"),
                        rs.getString("code"),
                        rs.getString("description")
                ),
                roleId
        ));
    }

    private String savePermissionReference(Permission permission) {
        Optional<String> existingId = findPermissionIdByCode(permission.getCode());
        String permissionId = existingId.orElse(permission.getId().value());
        if (existsPermissionById(permissionId)) {
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
        return permissionId;
    }

    private Optional<String> findPermissionIdByCode(String code) {
        List<String> ids = jdbcTemplate.query(
                "select id from sys_permissions where lower(code) = lower(?)",
                (rs, rowNum) -> rs.getString("id"),
                code
        );
        return ids.stream().findFirst();
    }

    private boolean existsPermissionById(String id) {
        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from sys_permissions where id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    private boolean existsById(String id) {
        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from sys_roles where id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }
}
