package com.linkyou.identity.infrastructure.persistence.repository;

import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.RoleId;
import com.linkyou.identity.domain.repository.RoleRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@ConditionalOnProperty(prefix = "identity.repository", name = "type", havingValue = "inmemory", matchIfMissing = true)
public class InMemoryRoleRepository implements RoleRepository {

    private final ConcurrentMap<String, Role> storage = new ConcurrentHashMap<>();

    @Override
    public Role save(Role role) {
        storage.put(role.getId().value(), role);
        return role;
    }

    @Override
    public Optional<Role> findById(RoleId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(storage.values());
    }
}
