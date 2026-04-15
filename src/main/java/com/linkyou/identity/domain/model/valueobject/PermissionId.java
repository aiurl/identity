package com.linkyou.identity.domain.model.valueobject;

import com.linkyou.identity.common.exception.DomainException;

import java.util.UUID;

public record PermissionId(String value) {

    public PermissionId {
        if (value == null || value.isBlank()) {
            throw new DomainException("Permission id cannot be blank");
        }
    }

    public static PermissionId newId() {
        return new PermissionId(UUID.randomUUID().toString());
    }
}
