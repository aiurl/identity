package com.linkyou.identity.application.query;

import com.linkyou.identity.application.query.dto.PermissionDto;

import java.util.List;

public record ListPermissionsQuery() implements Query<List<PermissionDto>> {
}
