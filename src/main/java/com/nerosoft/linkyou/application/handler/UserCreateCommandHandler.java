package com.nerosoft.linkyou.application.handler;

import org.springframework.stereotype.Component;

import com.nerosoft.linkyou.application.command.UserCreateCommand;
import com.nerosoft.linkyou.domain.aggregate.User;
import com.nerosoft.linkyou.domain.repository.UserRepository;

import an.awesome.pipelinr.Command;

/**
 * 用户创建命令处理器
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

            var exists = userRepository.findByAnyOf(dto.username(), dto.email(), dto.phone(), null);

            if (exists != null) {
                if (exists.getUsername().equals(dto.username())) {
                    throw new IllegalStateException("用户名已存在");
                }
                if (exists.getEmail() != null && exists.getEmail().equals(dto.email())) {
                    throw new IllegalStateException("邮箱已被使用");
                }
                if (exists.getPhone() != null && exists.getPhone().equals(dto.phone())) {
                    throw new IllegalStateException("手机号已被使用");
                }
            }

            var user = User.create(command.getData().username());

            user.setPassword(dto.password(), "init");

            userRepository.save(user);
            return user.getId();
        } catch (Exception ex) {
            throw new RuntimeException("创建用户失败: " + ex.getMessage(), ex);
        }
    }
}

