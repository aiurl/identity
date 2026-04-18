package com.nerosoft.linkyou.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nerosoft.linkyou.seedwork.JwtAuthenticationManager;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtAuthenticationManager jwtAuthenticationManager) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationManager = jwtAuthenticationManager;
    }

    @PostMapping("/token/grant")
    public ResponseEntity<TokenResponse> grantToken(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        return ResponseEntity.ok(buildTokenResponse(userDetails));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String username = jwtAuthenticationManager.extractSubject(request.refreshToken());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtAuthenticationManager.isTokenValid(request.refreshToken(), userDetails)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
            }

            return ResponseEntity.ok(buildTokenResponse(userDetails));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }

    private TokenResponse buildTokenResponse(UserDetails userDetails) {
        return new TokenResponse(
                jwtAuthenticationManager.generateAccessToken(userDetails),
                jwtAuthenticationManager.generateRefreshToken(userDetails),
                jwtAuthenticationManager.getAccessTokenExpiration(),
                jwtAuthenticationManager.getRoles(userDetails));
    }

    public record LoginRequest(String username, String password) {
    }

    public record RefreshTokenRequest(String refreshToken) {
    }

    public record TokenResponse(String accessToken, String refreshToken, long expiresIn, List<String> roles) {
    }
}
