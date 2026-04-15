package com.linkyou.identity.domain.model.valueobject;

import com.linkyou.identity.common.exception.DomainException;

import java.util.UUID;

public record RoleId(String value) {

    public RoleId {
        if (value == null || value.isBlank()) {
            throw new DomainException("Role id cannot be blank");
        }
    }

    public static RoleId newId() {
        return new RoleId(UUID.randomUUID().toString());
    }
}
