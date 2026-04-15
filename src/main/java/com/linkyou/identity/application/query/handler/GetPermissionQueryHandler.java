package com.linkyou.identity.application.query.handler;

import com.linkyou.identity.application.query.dto.PermissionView;
import com.linkyou.identity.domain.model.entity.Permission;
import com.linkyou.identity.domain.model.valueobject.PermissionId;
import com.linkyou.identity.domain.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GetPermissionQueryHandler {

    private final PermissionRepository permissionRepository;

    public GetPermissionQueryHandler(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Optional<PermissionView> findById(String id) {
        return permissionRepository.findById(new PermissionId(id)).map(this::toView);
    }

    public List<PermissionView> findAll() {
        return permissionRepository.findAll().stream().map(this::toView).toList();
    }

    private PermissionView toView(Permission permission) {
        return new PermissionView(permission.getId().value(), permission.getCode(), permission.getDescription());
    }
}
