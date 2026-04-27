package com.nerosoft.linkyou.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.nerosoft.linkyou.domain.valueobject.EmailAddress;
import com.nerosoft.linkyou.domain.valueobject.PhoneNumber;

class ContactValueObjectTests {

    @Test
    void emailAddress_normalizesAndValidates() {
        EmailAddress email = EmailAddress.of(" Alice@Example.com ");

        assertEquals("alice@example.com", email.value());
        assertThrows(IllegalArgumentException.class, () -> EmailAddress.of("bad-email"));
    }

    @Test
    void phoneNumber_normalizesAndValidates() {
        PhoneNumber phone = PhoneNumber.of(" +86 13800138000 ");

        assertEquals("+86 13800138000", phone.value());
        assertThrows(IllegalArgumentException.class, () -> PhoneNumber.of("abc"));
    }
}
