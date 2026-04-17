package com.linkyou.identity.application.service;

import com.linkyou.identity.application.query.dto.AuthTokenDto;
import com.linkyou.identity.application.query.dto.UserDto;
import com.linkyou.identity.common.exception.DomainException;
import com.linkyou.identity.domain.model.aggregate.User;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.repository.UserRepository;
import com.linkyou.identity.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class DefaultAuthApplicationService implements AuthApplicationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public DefaultAuthApplicationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDto register(String username, String nickname, String phone, String email, String password) {
        validateUniqueUserFields(username, phone, email);

        if (password == null || password.isBlank()) {
            throw new DomainException("Password cannot be blank");
        }

        String passwordSalt = generateSalt();
        String passwordHash = passwordEncoder.encode(password + passwordSalt);
        User user = User.register(username, nickname, phone, email, passwordHash, passwordSalt);
        user.assignRole(Role.create("USER", "Default user role"));

        if ("admin".equalsIgnoreCase(username)) {
            user.assignRole(Role.create("ADMIN", "System administrator role"));
        }

        User savedUser = userRepository.save(user);
        List<String> roles = savedUser.getRoles().stream().map(Role::getName).toList();
        return new UserDto(
                savedUser.getId().value(),
                savedUser.getUsername(),
                savedUser.getNickname(),
                savedUser.getPhone(),
                savedUser.getEmail().value(),
                savedUser.getAccessFailedCount(),
                savedUser.getLockoutEnd(),
                savedUser.getCreatedAt(),
                savedUser.getPasswordChangedTime(),
                savedUser.isActive(),
                roles
        );
    }

    @Override
    public AuthTokenDto login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DomainException("Invalid username or password"));

        if (user.isLocked()) {
            throw new DomainException("Account is temporarily locked");
        }

        if (!passwordEncoder.matches(password + user.getPasswordSalt(), user.getPasswordHash())) {
            user.markAccessFailed();
            userRepository.save(user);
            throw new DomainException("Invalid username or password");
        }

        user.resetAccessFailures();
        userRepository.save(user);

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(String::toUpperCase)
                .toList();

        return new AuthTokenDto(jwtService.generateToken(user.getUsername(), roles));
    }

    private void validateUniqueUserFields(String username, String phone, String email) {
        userRepository.findByUsername(username)
                .ifPresent(existingUser -> {
                    throw new DomainException("Username already exists");
                });

        userRepository.findByPhone(phone)
                .ifPresent(existingUser -> {
                    throw new DomainException("Phone already exists");
                });

        userRepository.findByEmail(email)
                .ifPresent(existingUser -> {
                    throw new DomainException("Email already exists");
                });
    }

    private String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
