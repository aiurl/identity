package com.linkyou.identity.application.query;

import com.linkyou.identity.application.query.dto.UserDto;

import java.util.Optional;

public record GetUserByIdQuery(String id) implements Query<Optional<UserDto>> {
}
