package com.linkyou.identity.infrastructure.persistence.repository;

import com.linkyou.identity.domain.model.aggregate.User;
import com.linkyou.identity.domain.model.valueobject.UserId;
import com.linkyou.identity.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final ConcurrentMap<String, User> storage = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        storage.put(user.getId().value(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return storage.values().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return Optional.empty();
        }
        return storage.values().stream()
                .filter(user -> phone.equalsIgnoreCase(user.getPhone()))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return storage.values().stream()
                .filter(user -> user.getEmail().value().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }
}
