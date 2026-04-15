package com.linkyou.identity.application.service;

import com.linkyou.identity.application.command.dto.LoginCommand;
import com.linkyou.identity.application.command.dto.RegisterUserCommand;
import com.linkyou.identity.application.query.dto.AuthTokenView;
import com.linkyou.identity.application.query.dto.UserView;
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
public class AuthApplicationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthApplicationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserView register(RegisterUserCommand command) {
        validateUniqueUserFields(command.username(), command.phone(), command.email());

        if (command.password() == null || command.password().isBlank()) {
            throw new DomainException("Password cannot be blank");
        }

        String passwordSalt = generateSalt();
        String passwordHash = passwordEncoder.encode(command.password() + passwordSalt);
        User user = User.register(command.username(), command.nickname(), command.phone(), command.email(), passwordHash, passwordSalt);
        user.assignRole(Role.create("USER", "Default user role"));

        if ("admin".equalsIgnoreCase(command.username())) {
            user.assignRole(Role.create("ADMIN", "System administrator role"));
        }

        userRepository.save(user);
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        return new UserView(
                user.getId().value(),
                user.getUsername(),
                user.getNickname(),
                user.getPhone(),
                user.getEmail().value(),
                user.getAccessFailedCount(),
                user.getLockoutEnd(),
                user.getCreatedAt(),
                user.getPasswordChangedTime(),
                user.isActive(),
                roles
        );
    }

    public AuthTokenView login(LoginCommand command) {
        User user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new DomainException("Invalid username or password"));

        if (user.isLocked()) {
            throw new DomainException("Account is temporarily locked");
        }

        if (!passwordEncoder.matches(command.password() + user.getPasswordSalt(), user.getPasswordHash())) {
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

        return new AuthTokenView(jwtService.generateToken(user.getUsername(), roles));
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
