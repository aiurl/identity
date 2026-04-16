package com.linkyou.identity.infrastructure.persistence.repository;

import com.linkyou.identity.domain.model.entity.Permission;
import com.linkyou.identity.domain.model.valueobject.PermissionId;
import com.linkyou.identity.domain.repository.PermissionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@ConditionalOnProperty(prefix = "identity.repository", name = "type", havingValue = "inmemory", matchIfMissing = true)
public class InMemoryPermissionRepository implements PermissionRepository {

    private final ConcurrentMap<String, Permission> storage = new ConcurrentHashMap<>();

    @Override
    public Permission save(Permission permission) {
        storage.put(permission.getId().value(), permission);
        return permission;
    }

    @Override
    public Optional<Permission> findById(PermissionId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }

    @Override
    public List<Permission> findAll() {
        return new ArrayList<>(storage.values());
    }
}
