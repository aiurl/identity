package com.linkyou.identity.domain.repository;

import com.linkyou.identity.domain.model.entity.Role;
import com.linkyou.identity.domain.model.valueobject.RoleId;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(RoleId id);

    List<Role> findAll();
}
