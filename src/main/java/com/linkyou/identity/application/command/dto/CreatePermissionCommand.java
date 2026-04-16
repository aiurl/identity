package com.linkyou.identity.application.command.dto;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.PermissionView;
import jakarta.validation.constraints.NotBlank;

public record CreatePermissionCommand(
        @NotBlank(message = "permission code不能为空") String code,
        String description
) implements Command<PermissionView> {
}
