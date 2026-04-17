package com.linkyou.identity.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequestDto(
        @NotBlank(message = "role name不能为空") String name,
        String description
) {
}
