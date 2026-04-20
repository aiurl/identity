package com.nerosoft.linkyou.facade.dtos;

/** 
 * 令牌颁发请求 DTO
 * @param username 用户名
 * @param password 密码
 * @param grantType 授权类型（如 "password"）
 * @param requestId 请求 ID（可选，用于追踪请求）
 */
public record TokenGrantRequestDto(String username, String password, String grantType, String requestId) {
}
