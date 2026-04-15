package com.linkyou.identity.application.command.handler;

import com.linkyou.identity.application.command.dto.CreateUserCommand;
import com.linkyou.identity.application.query.dto.UserView;
import com.linkyou.identity.common.exception.DomainException;
import com.linkyou.identity.domain.model.aggregate.User;
import com.linkyou.identity.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateUserCommandHandler {

    private final UserRepository userRepository;

    public CreateUserCommandHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserView handle(CreateUserCommand command) {
        userRepository.findByUsername(command.username())
                .ifPresent(existingUser -> {
                    throw new DomainException("Username already exists");
                });
        userRepository.findByPhone(command.phone())
                .ifPresent(existingUser -> {
                    throw new DomainException("Phone already exists");
                });
        userRepository.findByEmail(command.email())
                .ifPresent(existingUser -> {
                    throw new DomainException("Email already exists");
                });

        User user = User.register(command.username(), command.nickname(), command.phone(), command.email(), "", "");
        userRepository.save(user);
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
                List.of()
        );
    }
}
