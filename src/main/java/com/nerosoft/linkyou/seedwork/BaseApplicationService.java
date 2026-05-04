package com.nerosoft.linkyou.seedwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import an.awesome.pipelinr.Pipeline;

public abstract class BaseApplicationService implements ApplicationService {
    
    @Autowired
    protected Pipeline pipeline;

    protected String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("当前没有已认证的用户");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        if (principal instanceof String username) {
            return username;
        }
        throw new IllegalStateException("无法获取当前用户ID，不支持的认证主体类型: " + principal);
    }
}
