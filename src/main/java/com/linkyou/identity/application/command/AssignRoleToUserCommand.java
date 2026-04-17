package com.linkyou.identity.application.command;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.UserDto;
import jakarta.validation.constraints.NotBlank;

public record AssignRoleToUserCommand(
        @NotBlank(message = "userId不能为空") String userId,
        @NotBlank(message = "roleId不能为空") String roleId
) implements Command<UserDto> {
}
