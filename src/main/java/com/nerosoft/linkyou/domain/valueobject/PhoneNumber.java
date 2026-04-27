package com.nerosoft.linkyou.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public final class PhoneNumber {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+()\\-\\s]{6,20}$");

    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    public static PhoneNumber of(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be blank");
        }

        String normalized = raw.trim().replaceAll("\\s+", " ");
        if (!PHONE_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid phone format");
        }
        return new PhoneNumber(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PhoneNumber other)) {
            return false;
        }
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
