package com.nerosoft.linkyou.application.contract;

import com.nerosoft.linkyou.application.dto.TokenGrantRequestDto;
import com.nerosoft.linkyou.application.dto.TokenGrantResponseDto;

import reactor.core.publisher.Mono;

/**
 * 认证应用服务接口，提供用户认证相关的功能
 */
public interface AuthApplicationService {
    /**
     * 处理用户登录请求，验证用户凭据并生成 JWT 令牌
     *
     * @param request 包含用户名和密码的登录请求数据
     * @return 包含 JWT 令牌和相关信息的响应数据
     */
    Mono<TokenGrantResponseDto> grantAsync(TokenGrantRequestDto request);
}
