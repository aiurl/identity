package com.linkyou.identity.application.command;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.RoleDto;
import jakarta.validation.constraints.NotBlank;

public record CreateRoleCommand(
        @NotBlank(message = "role name不能为空") String name,
        String description
) implements Command<RoleDto> {
}
