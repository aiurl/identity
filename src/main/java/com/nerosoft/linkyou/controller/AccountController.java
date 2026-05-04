package com.nerosoft.linkyou.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nerosoft.linkyou.application.contract.UserApplicationService;
import com.nerosoft.linkyou.application.dto.UserCreateDto;
import com.nerosoft.linkyou.application.dto.UserDetailDto;
import com.nerosoft.linkyou.application.dto.UserPasswordChangeDto;
import com.nerosoft.linkyou.application.dto.UserProfileDto;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final UserApplicationService userApplicationService;

    public AccountController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    /**
     * 创建用户
     * @param data 包含创建用户所需的信息，必须包含 username 和 password 字段
     * @return 创建的用户 ID
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserCreateDto data) {
        try {
            String userId = userApplicationService.createAsync(data).block();
            UserDetailDto detail = userApplicationService.getAsync(userId).block();
            return ResponseEntity.created(URI.create("/api/account/" + userId)).body(detail);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            UserDetailDto detail = userApplicationService.getAsync(id).block();
            return ResponseEntity.ok(detail);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile() {
        UserProfileDto profile = userApplicationService.getProfileAsync().block();
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/password/change")
    public ResponseEntity<?> changePassword(@RequestBody UserPasswordChangeDto data) {
        if(data == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid request body"));
        }
        if(data.oldPassword == null || data.newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Both oldPassword and newPassword are required"));
        }
        userApplicationService.changePasswordAsync(data.oldPassword, data.newPassword).block();
        return ResponseEntity.noContent().build();
    }
    
}
