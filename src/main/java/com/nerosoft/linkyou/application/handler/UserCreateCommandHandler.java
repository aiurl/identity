package com.nerosoft.linkyou.application.handler;

import org.springframework.stereotype.Component;

import com.nerosoft.linkyou.application.command.UserCreateCommand;
import com.nerosoft.linkyou.domain.aggregate.User;
import com.nerosoft.linkyou.domain.repository.UserRepository;

import an.awesome.pipelinr.Command;

/**
 * Handler to process UserCreateCommand, responsible for creating a new user in the system.
 * It checks for existing users with the same username, email, or phone number to ensure uniqueness before saving the new user to the repository.
 */
@Component
public class UserCreateCommandHandler implements Command.Handler<UserCreateCommand, String> {
    private final UserRepository userRepository;

    public UserCreateCommandHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String handle(UserCreateCommand command) {

        try {
            var dto = command.getData();

            var exists = userRepository.findByAnyOf(dto.getUsername(), dto.getEmail(), dto.getPhone(), null);

            if (exists != null) {
                if (exists.getUsername().equals(dto.getUsername())) {
                    throw new IllegalStateException("用户名已存在");
                }
                if (exists.getEmail() != null && exists.getEmail().equals(dto.getEmail())) {
                    throw new IllegalStateException("邮箱已被使用");
                }
                if (exists.getPhone() != null && exists.getPhone().equals(dto.getPhone())) {
                    throw new IllegalStateException("手机号已被使用");
                }
            }

            var user = User.create(command.getData().getUsername());

            user.setPassword(dto.getPassword(), "init");

            if(command.getData().getEmail() != null) {
                user.setEmail(dto.getEmail());
            }
            if(command.getData().getPhone() != null) {
                user.setPhone(dto.getPhone());
            }

            userRepository.save(user);
            return user.getId();
        } catch (Exception ex) {
            throw new RuntimeException("创建用户失败: " + ex.getMessage(), ex);
        }
    }
}

