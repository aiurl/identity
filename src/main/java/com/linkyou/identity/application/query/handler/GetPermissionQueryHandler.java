package com.linkyou.identity.application.query.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.ListPermissionsQuery;
import com.linkyou.identity.application.query.dto.PermissionDto;
import com.linkyou.identity.domain.model.entity.Permission;
import com.linkyou.identity.domain.model.valueobject.PermissionId;
import com.linkyou.identity.domain.repository.PermissionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GetPermissionQueryHandler implements Command.Handler<ListPermissionsQuery, List<PermissionDto>> {

    private final PermissionRepository permissionRepository;

    public GetPermissionQueryHandler(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Optional<PermissionDto> findById(String id) {
        return permissionRepository.findById(new PermissionId(id)).map(this::toView);
    }

    @Override
    public List<PermissionDto> handle(ListPermissionsQuery query) {
        return permissionRepository.findAll().stream().map(this::toView).toList();
    }

    public List<PermissionDto> findAll() {
        return handle(new ListPermissionsQuery());
    }

    private PermissionDto toView(Permission permission) {
        return new PermissionDto(permission.getId().value(), permission.getCode(), permission.getDescription());
    }
}
