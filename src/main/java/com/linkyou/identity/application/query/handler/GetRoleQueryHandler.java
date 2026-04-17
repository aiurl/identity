package com.linkyou.identity.application.query.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.ListRolesQuery;
import com.linkyou.identity.application.query.dto.RoleDto;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.RoleId;
import com.linkyou.identity.domain.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GetRoleQueryHandler implements Command.Handler<ListRolesQuery, List<RoleDto>> {

    private final RoleRepository roleRepository;

    public GetRoleQueryHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<RoleDto> findById(String id) {
        return roleRepository.findById(new RoleId(id)).map(this::toView);
    }

    @Override
    public List<RoleDto> handle(ListRolesQuery query) {
        return roleRepository.findAll().stream().map(this::toView).toList();
    }

    public List<RoleDto> findAll() {
        return handle(new ListRolesQuery());
    }

    private RoleDto toView(Role role) {
        return new RoleDto(
                role.getId().value(),
                role.getName(),
                role.getDescription(),
                role.getPermissions().stream().map(permission -> permission.getCode()).toList()
        );
    }
}
