package com.linkyou.identity.application.query;

import com.linkyou.identity.application.query.dto.PermissionDto;

import java.util.Optional;

public record GetPermissionByIdQuery(String id) implements Query<Optional<PermissionDto>> {
}
