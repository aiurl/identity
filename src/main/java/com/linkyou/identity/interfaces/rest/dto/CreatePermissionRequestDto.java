package com.linkyou.identity.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePermissionRequestDto(
        @NotBlank(message = "permission code不能为空") String code,
        String description
) {
}
