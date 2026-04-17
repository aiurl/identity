package com.linkyou.identity.application.command.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.command.CreateRoleCommand;
import com.linkyou.identity.application.query.dto.RoleDto;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateRoleCommandHandler implements Command.Handler<CreateRoleCommand, RoleDto> {

    private final RoleRepository roleRepository;

    public CreateRoleCommandHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDto handle(CreateRoleCommand command) {
        Role role = Role.create(command.name(), command.description());
        Role savedRole = roleRepository.save(role);
        return new RoleDto(savedRole.getId().value(), savedRole.getName(), savedRole.getDescription(), List.of());
    }
}
