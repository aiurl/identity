package com.nerosoft.linkyou.application.implement;

import java.util.HashMap;
import java.util.Objects;

import com.nerosoft.linkyou.application.command.UserPasswordChangeCommand;
import com.nerosoft.linkyou.seedwork.CommandResult;
import com.nerosoft.linkyou.utility.Cryptography;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.nerosoft.linkyou.application.command.UserCreateCommand;
import com.nerosoft.linkyou.application.command.UserUpdateCommand;
import com.nerosoft.linkyou.application.contract.UserApplicationService;
import com.nerosoft.linkyou.application.dto.UserCreateDto;
import com.nerosoft.linkyou.application.dto.UserDetailDto;
import com.nerosoft.linkyou.application.dto.UserProfileDto;
import com.nerosoft.linkyou.application.query.UserDetailQuery;
import com.nerosoft.linkyou.seedwork.BaseApplicationService;

import reactor.core.publisher.Mono;

@Service
public class DefaultUserApplicationService extends BaseApplicationService implements UserApplicationService {
    private final ModelMapper modelMapper;

    public DefaultUserApplicationService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<String> createAsync(UserCreateDto data) {

        var command = new UserCreateCommand(data);

        pipeline.send(command);

        // Mono.fromCallable(() -> pipeline.send(command));
        return Mono.fromCallable(() -> pipeline.send(command)).map(result -> result);
    }

    @Override
    public Mono<Void> updateAsync(String id, HashMap<String, Object> data) {

        var command = new UserUpdateCommand(id, data);

        return Mono.fromCallable(() -> pipeline.send(command));
    }

    @Override
    public Mono<UserDetailDto> getAsync(String id) {

        var query = new UserDetailQuery(id, null);

        return Mono.fromCallable(() -> pipeline.send(query)).map(result -> modelMapper.map(result, UserDetailDto.class));
    }

    @Override
    public Mono<UserProfileDto> getProfileAsync() {
        var userId = getCurrentUserId();
        var query = new UserDetailQuery(userId, null);

        return Mono.fromCallable(() -> pipeline.send(query)).map(result -> modelMapper.map(result, UserProfileDto.class));
    }

    @Override
    public Mono<Void> changePasswordAsync(String oldPassword, String newPassword) {
        try {
            if (oldPassword == null || oldPassword.isEmpty()) {
                throw new IllegalArgumentException("Old password cannot be null or empty");
            }
            if (newPassword == null || newPassword.isEmpty()) {
                throw new IllegalArgumentException("New password cannot be null or empty");
            }

            var id = getCurrentUserId();

            var user = Mono.fromCallable(() -> pipeline.send(new UserDetailQuery(id, null))).map(CommandResult::getResult).block();

            if (user == null) {
                throw new EntityNotFoundException("User not found: " + id);
            }

            var passwordHash = Cryptography.AES.encrypt(oldPassword, user.getPasswordSalt());

            if (!Objects.equals(passwordHash, user.getPasswordHash())) {
                throw new IllegalArgumentException("Old password is incorrect");
            }

            var command = new UserPasswordChangeCommand(id, newPassword, "change");

            return Mono.fromCallable(() -> pipeline.send(command));
        } catch (Exception ex) {
            return Mono.error(ex);
        }
    }

    @Override
    public Mono<Void> resetPasswordAsync(String username, String password, String verifyCode) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (verifyCode == null || verifyCode.isEmpty()) {
            throw new IllegalArgumentException("Verify code cannot be null or empty");
        } else {
            // TODO: 校验验证码
            System.out.println("Verify code: " + verifyCode);
        }

        return Mono.fromCallable(() -> {
            var query = new UserDetailQuery(null, username);
            var user = pipeline.send(query);
            var command = new UserPasswordChangeCommand(user.getResult().getId(), password, "reset");
            pipeline.send(command);
            return null;
        });
    }

}
