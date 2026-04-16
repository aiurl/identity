package com.linkyou.identity.application.command.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.command.dto.AssignRoleToUserCommand;
import com.linkyou.identity.application.query.dto.UserView;
import com.linkyou.identity.common.exception.DomainException;
import com.linkyou.identity.domain.model.aggregate.User;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.RoleId;
import com.linkyou.identity.domain.model.valueobject.UserId;
import com.linkyou.identity.domain.repository.RoleRepository;
import com.linkyou.identity.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class AssignRoleToUserCommandHandler implements Command.Handler<AssignRoleToUserCommand, UserView> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AssignRoleToUserCommandHandler(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserView handle(AssignRoleToUserCommand command) {
        User user = userRepository.findById(new UserId(command.userId()))
                .orElseThrow(() -> new DomainException("User not found"));
        Role role = roleRepository.findById(new RoleId(command.roleId()))
                .orElseThrow(() -> new DomainException("Role not found"));

        user.assignRole(role);
        User savedUser = userRepository.save(user);

        return new UserView(
                savedUser.getId().value(),
                savedUser.getUsername(),
                savedUser.getNickname(),
                savedUser.getPhone(),
                savedUser.getEmail().value(),
                savedUser.getAccessFailedCount(),
                savedUser.getLockoutEnd(),
                savedUser.getCreatedAt(),
                savedUser.getPasswordChangedTime(),
                savedUser.isActive(),
                savedUser.getRoles().stream().map(Role::getName).toList()
        );
    }
}
