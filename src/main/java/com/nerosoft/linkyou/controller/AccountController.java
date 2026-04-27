package com.nerosoft.linkyou.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nerosoft.linkyou.application.contract.UserApplicationService;
import com.nerosoft.linkyou.application.dto.UserCreateDto;
import com.nerosoft.linkyou.application.dto.UserDetailDto;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final UserApplicationService userApplicationService;

    public AccountController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserCreateDto data) {
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

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "roles", authentication.getAuthorities().stream().map(Object::toString).toList());
    }
}
