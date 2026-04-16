package com.linkyou.identity.domain.model.entity;

import com.linkyou.identity.common.exception.DomainException;
import com.linkyou.identity.domain.model.valueobject.PermissionId;

public class Permission {

    private final PermissionId id;
    private final String code;
    private final String description;

    private Permission(PermissionId id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }

    public static Permission create(String code, String description) {
        if (code == null || code.isBlank()) {
            throw new DomainException("Permission code cannot be blank");
        }
        return new Permission(PermissionId.newId(), code.trim().toUpperCase(), description == null ? "" : description.trim());
    }

    public static Permission reconstitute(String id, String code, String description) {
        return new Permission(new PermissionId(id), code.trim().toUpperCase(), description == null ? "" : description.trim());
    }

    public PermissionId getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
