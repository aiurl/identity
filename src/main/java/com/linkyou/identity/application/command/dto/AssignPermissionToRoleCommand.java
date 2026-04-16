package com.linkyou.identity.application.command.dto;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.RoleView;
import jakarta.validation.constraints.NotBlank;

public record AssignPermissionToRoleCommand(
        @NotBlank(message = "roleId不能为空") String roleId,
        @NotBlank(message = "permissionId不能为空") String permissionId
) implements Command<RoleView> {
}
