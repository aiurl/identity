package com.nerosoft.linkyou.application.dto;

import lombok.Data;

/**
 * 用户个人信息 DTO
 */
@Data
public class UserProfileDto {
    private String id;
    private String username;
    private String email;
    private String phone;
}
