package com.linkyou.identity.application.command.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.command.dto.CreatePermissionCommand;
import com.linkyou.identity.application.query.dto.PermissionView;
import com.linkyou.identity.domain.model.entity.Permission;
import com.linkyou.identity.domain.repository.PermissionRepository;
import org.springframework.stereotype.Component;

@Component
public class CreatePermissionCommandHandler implements Command.Handler<CreatePermissionCommand, PermissionView> {

    private final PermissionRepository permissionRepository;

    public CreatePermissionCommandHandler(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PermissionView handle(CreatePermissionCommand command) {
        Permission permission = Permission.create(command.code(), command.description());
        Permission savedPermission = permissionRepository.save(permission);
        return new PermissionView(savedPermission.getId().value(), savedPermission.getCode(), savedPermission.getDescription());
    }
}
