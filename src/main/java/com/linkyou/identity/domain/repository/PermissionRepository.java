package com.linkyou.identity.domain.repository;

import com.linkyou.identity.domain.model.entity.Permission;
import com.linkyou.identity.domain.model.valueobject.PermissionId;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository {

    Permission save(Permission permission);

    Optional<Permission> findById(PermissionId id);

    List<Permission> findAll();
}
