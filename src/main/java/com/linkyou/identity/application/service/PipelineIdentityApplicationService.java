package com.linkyou.identity.application.service;

import an.awesome.pipelinr.Pipeline;
import com.linkyou.identity.application.command.AssignPermissionToRoleCommand;
import com.linkyou.identity.application.command.AssignRoleToUserCommand;
import com.linkyou.identity.application.command.CreatePermissionCommand;
import com.linkyou.identity.application.command.CreateRoleCommand;
import com.linkyou.identity.application.command.CreateUserCommand;
import com.linkyou.identity.application.query.GetPermissionByIdQuery;
import com.linkyou.identity.application.query.GetRoleByIdQuery;
import com.linkyou.identity.application.query.GetUserByIdQuery;
import com.linkyou.identity.application.query.ListPermissionsQuery;
import com.linkyou.identity.application.query.ListRolesQuery;
import com.linkyou.identity.application.query.ListUsersQuery;
import com.linkyou.identity.application.query.dto.PermissionDto;
import com.linkyou.identity.application.query.dto.RoleDto;
import com.linkyou.identity.application.query.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PipelineIdentityApplicationService implements IdentityApplicationService {

    private final Pipeline pipeline;

    public PipelineIdentityApplicationService(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public UserDto createUser(String username, String nickname, String phone, String email) {
        return pipeline.send(new CreateUserCommand(username, nickname, phone, email));
    }

    @Override
    public Optional<UserDto> getUserById(String id) {
        return pipeline.send(new GetUserByIdQuery(id));
    }

    @Override
    public List<UserDto> listUsers() {
        return pipeline.send(new ListUsersQuery());
    }

    @Override
    public UserDto assignRoleToUser(String userId, String roleId) {
        return pipeline.send(new AssignRoleToUserCommand(userId, roleId));
    }

    @Override
    public RoleDto createRole(String name, String description) {
        return pipeline.send(new CreateRoleCommand(name, description));
    }

    @Override
    public Optional<RoleDto> getRoleById(String id) {
        return pipeline.send(new GetRoleByIdQuery(id));
    }

    @Override
    public List<RoleDto> listRoles() {
        return pipeline.send(new ListRolesQuery());
    }

    @Override
    public RoleDto assignPermissionToRole(String roleId, String permissionId) {
        return pipeline.send(new AssignPermissionToRoleCommand(roleId, permissionId));
    }

    @Override
    public PermissionDto createPermission(String code, String description) {
        return pipeline.send(new CreatePermissionCommand(code, description));
    }

    @Override
    public Optional<PermissionDto> getPermissionById(String id) {
        return pipeline.send(new GetPermissionByIdQuery(id));
    }

    @Override
    public List<PermissionDto> listPermissions() {
        return pipeline.send(new ListPermissionsQuery());
    }
}
