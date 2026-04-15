package com.linkyou.identity.application.command.handler;

import com.linkyou.identity.application.command.dto.CreateRoleCommand;
import com.linkyou.identity.application.query.dto.RoleView;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateRoleCommandHandler {

    private final RoleRepository roleRepository;

    public CreateRoleCommandHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleView handle(CreateRoleCommand command) {
        Role role = Role.create(command.name(), command.description());
        roleRepository.save(role);
        return new RoleView(role.getId().value(), role.getName(), role.getDescription(), List.of());
    }
}
