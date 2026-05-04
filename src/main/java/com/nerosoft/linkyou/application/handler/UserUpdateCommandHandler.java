package com.nerosoft.linkyou.application.handler;

import org.springframework.stereotype.Component;

import com.nerosoft.linkyou.application.command.UserUpdateCommand;
import com.nerosoft.linkyou.domain.repository.UserRepository;

import an.awesome.pipelinr.Command;

@Component
public class UserUpdateCommandHandler implements Command.Handler<UserUpdateCommand, Void> {

    private final UserRepository userRepository;

    public UserUpdateCommandHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Void handle(UserUpdateCommand command) {
        var user = userRepository.findById(command.getId());
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + command.getId());
        }
        var data = command.getData();
        if (data.containsKey("email")) {
            user.changeEmail((String) data.get("email"));
        }
        if (data.containsKey("phone")) {
            user.changePhone((String) data.get("phone"));
        }
        userRepository.save(user);
        return null;
    }
}
