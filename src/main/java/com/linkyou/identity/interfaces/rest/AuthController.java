package com.linkyou.identity.interfaces.rest;

import com.linkyou.identity.application.query.dto.AuthTokenDto;
import com.linkyou.identity.application.query.dto.UserDto;
import com.linkyou.identity.application.service.AuthApplicationService;
import com.linkyou.identity.interfaces.rest.dto.LoginRequestDto;
import com.linkyou.identity.interfaces.rest.dto.RegisterUserRequestDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @PostMapping("/register")
    public Mono<UserDto> register(@Valid @RequestBody RegisterUserRequestDto request) {
        return authApplicationService.registerAsync(
                request.username(),
                request.nickname(),
                request.phone(),
                request.email(),
                request.password()
        );
    }

    @PostMapping("/login")
    public Mono<AuthTokenDto> login(@Valid @RequestBody LoginRequestDto request) {
        return authApplicationService.loginAsync(request.username(), request.password());
    }
}
