package com.linkyou.identity.domain.model.valueobject;

import com.linkyou.identity.common.exception.DomainException;

import java.util.UUID;

public record UserId(String value) {

    public UserId {
        if (value == null || value.isBlank()) {
            throw new DomainException("User id cannot be blank");
        }
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID().toString());
    }
}
