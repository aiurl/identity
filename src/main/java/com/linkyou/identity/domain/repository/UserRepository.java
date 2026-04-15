package com.linkyou.identity.domain.repository;

import com.linkyou.identity.domain.model.aggregate.User;
import com.linkyou.identity.domain.model.valueobject.UserId;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByUsername(String username);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    List<User> findAll();
}
