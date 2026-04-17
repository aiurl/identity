package com.linkyou.identity.application.query;

import com.linkyou.identity.application.query.dto.UserDto;

import java.util.List;

public record ListUsersQuery() implements Query<List<UserDto>> {
}
