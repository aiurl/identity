package com.linkyou.identity.application.query.handler;

import com.linkyou.identity.application.query.dto.RoleView;
import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.RoleId;
import com.linkyou.identity.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GetRoleQueryHandler {

    private final RoleRepository roleRepository;

    public GetRoleQueryHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<RoleView> findById(String id) {
        return roleRepository.findById(new RoleId(id)).map(this::toView);
    }

    public List<RoleView> findAll() {
        return roleRepository.findAll().stream().map(this::toView).toList();
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
