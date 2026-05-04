package com.nerosoft.linkyou.application.dto;

/**
 * 用户创建 DTO
 * 
 * @param username 用户名
 * @param password 密码
 * @param email    电子邮件地址
 * @param phone    电话号码
 * @param nickname 昵称
 */
public record UserCreateDto(String username, String password, String email, String phone, String nickname) {
}
