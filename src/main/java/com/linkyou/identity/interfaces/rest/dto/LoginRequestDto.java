package com.linkyou.identity.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "username不能为空") String username,
        @NotBlank(message = "password不能为空") String password
) {
}
