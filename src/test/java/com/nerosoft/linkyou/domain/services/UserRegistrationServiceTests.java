package com.nerosoft.linkyou.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.nerosoft.linkyou.domain.aggregate.User;
import com.nerosoft.linkyou.domain.service.UserRegistrationService;
import com.nerosoft.linkyou.persistence.repository.InMemoryUserRepository;

class UserRegistrationServiceTests {

    @Test
    void register_createsUserAndPersistsIt() throws Exception {
        InMemoryUserRepository repository = new InMemoryUserRepository();
        UserRegistrationService service = new UserRegistrationService(repository);

        User user = service.register("alice", "Secret123!", "alice@example.com", "+86 13800138000", "Alice");

        assertNotNull(user.getId());
        assertEquals("alice", user.getUsername());
        assertEquals("alice@example.com", user.getEmail());
        assertTrue(repository.findById(user.getId()) != null);
    }

    @Test
    void register_rejectsDuplicateUsername() throws Exception {
        InMemoryUserRepository repository = new InMemoryUserRepository();
        UserRegistrationService service = new UserRegistrationService(repository);

        service.register("alice", "Secret123!", "alice@example.com", null, null);

        assertThrows(IllegalStateException.class,
                () -> service.register("alice", "Secret123!", "other@example.com", null, null));
    }
}
