package com.linkyou.identity.application.command.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.command.dto.AssignPermissionToRoleCommand;
import com.linkyou.identity.application.query.dto.RoleView;
import com.linkyou.identity.common.exception.DomainException;
import com.linkyou.identity.domain.model.entity.Permission;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.PermissionId;
import com.linkyou.identity.domain.model.valueobject.RoleId;
import com.linkyou.identity.domain.repository.PermissionRepository;
import com.linkyou.identity.domain.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class AssignPermissionToRoleCommandHandler implements Command.Handler<AssignPermissionToRoleCommand, RoleView> {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public AssignPermissionToRoleCommandHandler(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public RoleView handle(AssignPermissionToRoleCommand command) {
        Role role = roleRepository.findById(new RoleId(command.roleId()))
                .orElseThrow(() -> new DomainException("Role not found"));
        Permission permission = permissionRepository.findById(new PermissionId(command.permissionId()))
                .orElseThrow(() -> new DomainException("Permission not found"));

        role.assignPermission(permission);
        Role savedRole = roleRepository.save(role);

        return new RoleView(
                savedRole.getId().value(),
                savedRole.getName(),
                savedRole.getDescription(),
                savedRole.getPermissions().stream().map(Permission::getCode).toList()
        );
    }
}
