package com.linkyou.identity.application.query;

import com.linkyou.identity.application.query.dto.RoleDto;

import java.util.Optional;

public record GetRoleByIdQuery(String id) implements Query<Optional<RoleDto>> {
}
