package com.linkyou.identity.application.command.handler;

import com.linkyou.identity.application.query.dto.RoleView;
import com.linkyou.identity.common.exception.DomainException;
import com.linkyou.identity.domain.model.entity.Permission;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.PermissionId;
import com.linkyou.identity.domain.model.valueobject.RoleId;
import com.linkyou.identity.domain.repository.PermissionRepository;
import com.linkyou.identity.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class AssignPermissionToRoleCommandHandler {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public AssignPermissionToRoleCommandHandler(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public RoleView handle(String roleId, String permissionId) {
        Role role = roleRepository.findById(new RoleId(roleId))
                .orElseThrow(() -> new DomainException("Role not found"));
        Permission permission = permissionRepository.findById(new PermissionId(permissionId))
                .orElseThrow(() -> new DomainException("Permission not found"));

        role.assignPermission(permission);
        roleRepository.save(role);

        return new RoleView(
                role.getId().value(),
                role.getName(),
                role.getDescription(),
                role.getPermissions().stream().map(Permission::getCode).toList()
        );
    }
}
