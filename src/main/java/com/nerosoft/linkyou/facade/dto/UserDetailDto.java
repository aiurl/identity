package com.nerosoft.linkyou.facade.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.Data;

@Data
public class UserDetailDto {
    private String id;
    private String username;
    private Optional<String> email = Optional.empty();
    private Optional<String> phone = Optional.empty();
    private Optional<String> nickname = Optional.empty();
    private Optional<String> avatarUrl = Optional.empty();
    private LocalDateTime createdAt;
    private Optional<LocalDateTime> lockoutEnd = Optional.empty();
    private List<String> roles = List.of();
    private List<String> authorities = List.of();
}
