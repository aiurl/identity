package com.linkyou.identity.application.query;

import com.linkyou.identity.application.query.dto.RoleDto;

import java.util.List;

public record ListRolesQuery() implements Query<List<RoleDto>> {
}
