package com.nerosoft.linkyou.domain.service;

import java.util.Objects;

import com.nerosoft.linkyou.domain.aggregate.User;
import com.nerosoft.linkyou.domain.repository.UserRepository;

public class UserRegistrationService {
    private final UserRepository userRepository;

    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository cannot be null");
    }

    public User register(String username,
                         String password,
                         String email,
                         String phone,
                         String nickname) throws Exception {
        String normalizedUsername = username == null ? null : username.trim();
        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new IllegalStateException("Username already exists");
        }

        User user = User.create(normalizedUsername);
        user.changeEmail(email);
        user.changePhone(phone);
        user.updateProfile(nickname, null);
        user.changePassword(password, "initial");
        userRepository.save(user);
        return user;
    }
}
