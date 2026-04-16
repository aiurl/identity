package com.linkyou.identity.domain.model.entity;

import com.linkyou.identity.common.exception.DomainException;
import com.linkyou.identity.domain.model.valueobject.RoleId;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Role {

    private final RoleId id;
    private final String name;
    private final String description;
    private final Set<Permission> permissions;

    private Role(RoleId id, String name, String description, Set<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = permissions;
    }

    public static Role create(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Role name cannot be blank");
        }
        return new Role(RoleId.newId(), name.trim(), description == null ? "" : description.trim(), new HashSet<>());
    }

    public static Role reconstitute(String id, String name, String description, Set<Permission> permissions) {
        return new Role(new RoleId(id), name, description == null ? "" : description, new HashSet<>(permissions == null ? Set.of() : permissions));
    }

    public void assignPermission(Permission permission) {
        if (permission != null) {
            permissions.add(permission);
        }
    }

    public RoleId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }
}
