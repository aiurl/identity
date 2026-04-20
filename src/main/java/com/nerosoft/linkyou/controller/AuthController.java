package com.nerosoft.linkyou.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nerosoft.linkyou.facade.contract.AuthApplicationService;
import com.nerosoft.linkyou.facade.dto.TokenGrantRequestDto;
import com.nerosoft.linkyou.facade.dto.TokenGrantResponseDto;
import com.nerosoft.linkyou.seedwork.JwtAuthenticationManager;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final AuthApplicationService _service;

    public AuthController(AuthenticationManager authenticationManager,
            AuthApplicationService service,
            JwtAuthenticationManager jwtAuthenticationManager) {
        this.authenticationManager = authenticationManager;
        _service = service;
        this.jwtAuthenticationManager = jwtAuthenticationManager;
    }

    @PostMapping("/token/grant")
    public ResponseEntity<TokenGrantResponseDto> grantToken(@RequestBody TokenGrantRequestDto request) {
        return ResponseEntity.ok(_service.grantAsync(request).block());
        // try {
        // authenticationManager.authenticate(
        // new UsernamePasswordAuthenticationToken(request.username(),
        // request.password()));
        // } catch (BadCredentialsException ex) {
        // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username
        // or password");
        // }

        // UserDetails userDetails =
        // userDetailsService.loadUserByUsername(request.username());
        // return ResponseEntity.ok(buildTokenResponse(userDetails));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenGrantResponseDto> refreshToken(@RequestParam String token) {
        return ResponseEntity.ok(_service.grantAsync(new TokenGrantRequestDto(null, token, "refresh_token", null)).block());
    }

    private TokenGrantResponseDto buildTokenResponse(UserDetails userDetails) {
        return new TokenGrantResponseDto(
                jwtAuthenticationManager.generateAccessToken(userDetails),
                jwtAuthenticationManager.generateRefreshToken(userDetails),
                "Bearer",
                jwtAuthenticationManager.getAccessTokenExpiration(),
                System.currentTimeMillis() / 1000,
                userDetails.getUsername(),
                userDetails.getUsername());
    }
}
