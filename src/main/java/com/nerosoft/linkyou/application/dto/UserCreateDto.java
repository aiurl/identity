package com.nerosoft.linkyou.application.dto;

import lombok.Data;

/**
 * Data transfer object for creating a new user.
 * Required: username, password.
 */
@Data
public class UserCreateDto {
    /**
     * Unique username.
     */
    private String username;

    /**
     * The password to login.
     */
    private String password;

    /**
     * Email address
     */
    private String email;

    /**
     * Phone number
     */
    private String phone;

    /**
     * Gets or sets the nickname of user.
     */
    private String nickname;
}