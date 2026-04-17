package com.linkyou.identity.application.command;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.UserDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserCommand(
        @NotBlank(message = "username不能为空") String username,
        String nickname,
        @Pattern(regexp = "^$|^\\+?[0-9]{6,20}$", message = "phone格式不正确") String phone,
        @NotBlank(message = "email不能为空")
        @Email(message = "email格式不正确") String email,
        @NotBlank(message = "password不能为空")
        @Size(min = 6, message = "password长度不能少于6位") String password
) implements Command<UserDto> {
}
