package com.nerosoft.linkyou.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * Data transfer object for user details.
 */
@Data
public class UserDetailDto {
    /**
     * The identifier of user.
     */
    private String id;
    private String username;
    private String email;
    private String phone;
    private String nickname;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime lockoutEnd = null;
    private List<String> roles = List.of();
    private List<String> authorities = List.of();
}
