package com.linkyou.identity.application.command.handler;

import com.linkyou.identity.application.query.dto.UserView;
import com.linkyou.identity.common.exception.DomainException;
import com.linkyou.identity.domain.model.aggregate.User;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.RoleId;
import com.linkyou.identity.domain.model.valueobject.UserId;
import com.linkyou.identity.domain.repository.RoleRepository;
import com.linkyou.identity.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AssignRoleToUserCommandHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AssignRoleToUserCommandHandler(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserView handle(String userId, String roleId) {
        User user = userRepository.findById(new UserId(userId))
                .orElseThrow(() -> new DomainException("User not found"));
        Role role = roleRepository.findById(new RoleId(roleId))
                .orElseThrow(() -> new DomainException("Role not found"));

        user.assignRole(role);
        userRepository.save(user);

        return new UserView(
                user.getId().value(),
                user.getUsername(),
                user.getNickname(),
                user.getPhone(),
                user.getEmail().value(),
                user.getAccessFailedCount(),
                user.getLockoutEnd(),
                user.getCreatedAt(),
                user.getPasswordChangedTime(),
                user.isActive(),
                user.getRoles().stream().map(Role::getName).toList()
        );
    }
}
