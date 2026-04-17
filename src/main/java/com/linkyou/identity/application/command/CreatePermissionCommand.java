package com.linkyou.identity.application.command;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.PermissionDto;
import jakarta.validation.constraints.NotBlank;

public record CreatePermissionCommand(
        @NotBlank(message = "permission code不能为空") String code,
        String description
) implements Command<PermissionDto> {
}
