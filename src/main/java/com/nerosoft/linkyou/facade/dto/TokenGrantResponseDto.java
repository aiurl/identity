package com.nerosoft.linkyou.facade.dto;

/** 
 * 令牌颁发响应 DTO
 * @param accessToken 访问令牌
 * @param refreshToken 刷新令牌
 * @param tokenType 令牌类型（如 "Bearer"）
 * @param expiresIn 以秒为单位的过期时间
 * @param issuedAt 以秒为单位的颁发时间
 * @param userId 用户ID
 * @param username 用户名
 */
public record TokenGrantResponseDto(String accessToken, String refreshToken, String tokenType, long expiresIn, long issuedAt, String userId, String username) {
}
