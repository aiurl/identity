package com.nerosoft.linkyou.application.dto;

import lombok.Data;

/**
 * 用户密码修改 DTO
 */
@Data
public class UserPasswordChangeDto {
    public String oldPassword;
    public String newPassword;
}
