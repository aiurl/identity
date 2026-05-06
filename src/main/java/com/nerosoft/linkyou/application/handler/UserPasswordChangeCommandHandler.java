package com.nerosoft.linkyou.application.handler;

import an.awesome.pipelinr.Command;
import com.nerosoft.linkyou.application.command.UserPasswordChangeCommand;
import com.nerosoft.linkyou.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Handler for processing UserPasswordChangeCommand, responsible for changing the user's password based on the provided command data.
 */
public final class UserPasswordChangeCommandHandler implements Command.Handler<UserPasswordChangeCommand, Void> {
    private final UserRepository repository;

    /**
     * Initializes a new instance of UserPasswordChangeCommandHandler with the specified UserRepository.
     * @param repository The user repository instance, cannot be null.
     */
    public UserPasswordChangeCommandHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Void handle(UserPasswordChangeCommand command) {
        var user = repository.findById(command.getId());
        if (user == null) {
            throw new EntityNotFoundException("User not found: " + command.getId());
        }
        try {
            user.changePassword(command.getPassword(), command.getAction());
            repository.save(user);
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to change password: " + e.getMessage(), e);
        }
    }
}
