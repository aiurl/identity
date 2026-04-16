package com.linkyou.identity.application.command.dto;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.UserView;
import jakarta.validation.constraints.NotBlank;

public record AssignRoleToUserCommand(
        @NotBlank(message = "userId不能为空") String userId,
        @NotBlank(message = "roleId不能为空") String roleId
) implements Command<UserView> {
}
