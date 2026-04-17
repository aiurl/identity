package com.linkyou.identity.application.query.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto(
        String id,
        String username,
        String nickname,
        String phone,
        String email,
        int accessFailedCount,
        LocalDateTime lockoutEnd,
        LocalDateTime createdAt,
        LocalDateTime passwordChangedTime,
        boolean active,
        List<String> roles
) {
}
