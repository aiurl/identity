package com.nerosoft.linkyou.persistence.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.nerosoft.linkyou.domain.aggregate.User;
import com.nerosoft.linkyou.domain.repository.UserRepository;

@Repository
@Primary
@ConditionalOnProperty(name = "repository.type", havingValue = "memory", matchIfMissing = true)
public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> usersById = new ConcurrentHashMap<>();

    @Override
    public void  save(User user) {
    }

    @Override
    public User findById(String id) {
        return usersById.get(id);
    }

    @Override
    public User findByUsername(String username) {
        if (username == null) {
            return null;
        }
        return usersById.values().stream()
                .filter(user -> user.getUsername() != null && user.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }

    @Override
    public void delete(User user) {
        if (user != null) {
            usersById.remove(user.getId());
        }
    }

    @Override
    public User findByAnyOf(String username, String email, String phone, String excludeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByAnyOf'");
    }
}
