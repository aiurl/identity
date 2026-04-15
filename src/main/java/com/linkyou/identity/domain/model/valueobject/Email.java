package com.linkyou.identity.domain.model.valueobject;

import com.linkyou.identity.common.exception.DomainException;

public record Email(String value) {

    public Email {
        if (value == null || !value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new DomainException("Invalid email address");
        }
        value = value.trim().toLowerCase();
    }
}
