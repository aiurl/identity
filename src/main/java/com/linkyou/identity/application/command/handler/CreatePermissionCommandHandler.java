package com.linkyou.identity.application.command.handler;

import com.linkyou.identity.application.command.dto.CreatePermissionCommand;
import com.linkyou.identity.application.query.dto.PermissionView;
import com.linkyou.identity.domain.model.entity.Permission;
import com.linkyou.identity.domain.repository.PermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class CreatePermissionCommandHandler {

    private final PermissionRepository permissionRepository;

    public CreatePermissionCommandHandler(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public PermissionView handle(CreatePermissionCommand command) {
        Permission permission = Permission.create(command.code(), command.description());
        permissionRepository.save(permission);
        return new PermissionView(permission.getId().value(), permission.getCode(), permission.getDescription());
    }
}
