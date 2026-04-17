package com.linkyou.identity.interfaces.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequestDto(
        @NotBlank(message = "username不能为空") String username,
        String nickname,
        @Pattern(regexp = "^$|^\\+?[0-9]{6,20}$", message = "phone格式不正确") String phone,
        @NotBlank(message = "email不能为空")
        @Email(message = "email格式不正确") String email
) {
}
