package com.linkyou.identity.application.query.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.ListRolesQuery;
import com.linkyou.identity.application.query.dto.RoleView;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.RoleId;
import com.linkyou.identity.domain.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GetRoleQueryHandler implements Command.Handler<ListRolesQuery, List<RoleView>> {

    private final RoleRepository roleRepository;

    public GetRoleQueryHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<RoleView> findById(String id) {
        return roleRepository.findById(new RoleId(id)).map(this::toView);
    }

    @Override
    public List<RoleView> handle(ListRolesQuery query) {
        return roleRepository.findAll().stream().map(this::toView).toList();
    }

    public List<RoleView> findAll() {
        return handle(new ListRolesQuery());
    }

    private RoleView toView(Role role) {
        return new RoleView(
                role.getId().value(),
                role.getName(),
                role.getDescription(),
                role.getPermissions().stream().map(permission -> permission.getCode()).toList()
        );
    }
}
