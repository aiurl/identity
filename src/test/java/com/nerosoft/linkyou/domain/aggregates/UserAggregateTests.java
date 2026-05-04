package com.nerosoft.linkyou.domain.aggregates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.nerosoft.linkyou.domain.aggregate.User;

class UserAggregateTests {

    @Test
    void createUser_assignsIdentityAndUsername() {
        User user = User.create("alice");

        assertNotNull(user.getId());
        assertEquals("alice", user.getUsername());
        assertTrue(user.getEvents().isEmpty());
    }

    @Test
    void changeEmail_updatesValueAndValidatesFormat() {
        User user = User.create("alice");

        user.changeEmail("alice@example.com");
        assertEquals("alice@example.com", user.getEmail());

        assertThrows(IllegalArgumentException.class, () -> user.changeEmail("bad-email"));
    }

    @Test
    void changePassword_raisesDomainEvent() throws Exception {
        User user = User.create("alice");

        user.changePassword("Secret123!", "change");

        assertFalse(user.getEvents().isEmpty());
        assertTrue(user.getPasswordChangedAt().isPresent());
    }

    @Test
    void domainEvents_areReadOnlyAndClearable() {
        User user = User.create("alice");
        List<?> events = user.getEvents();

        assertThrows(UnsupportedOperationException.class, () -> events.add(null));

        user.clearEvents();
        assertTrue(user.getEvents().isEmpty());
    }
}
