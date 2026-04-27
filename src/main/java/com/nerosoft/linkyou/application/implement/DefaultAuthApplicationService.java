package com.nerosoft.linkyou.application.implement;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nerosoft.linkyou.application.contract.AuthApplicationService;
import com.nerosoft.linkyou.application.dto.TokenGrantRequestDto;
import com.nerosoft.linkyou.application.dto.TokenGrantResponseDto;
import com.nerosoft.linkyou.seedwork.BaseApplicationService;
import com.nerosoft.linkyou.seedwork.JwtAuthenticationManager;

import reactor.core.publisher.Mono;

@Service
public final class DefaultAuthApplicationService extends BaseApplicationService implements AuthApplicationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    public DefaultAuthApplicationService(AuthenticationManager authenticationManager,
                                         UserDetailsService userDetailsService,
                                         JwtAuthenticationManager jwtAuthenticationManager) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationManager = jwtAuthenticationManager;
    }

    @Override
    public Mono<TokenGrantResponseDto> grantAsync(TokenGrantRequestDto request) {
        return Mono.fromCallable(() -> {
            try {
                if ("refresh_token".equalsIgnoreCase(request.grantType())) {
                    String refreshToken = request.password();
                    String username = jwtAuthenticationManager.extractSubject(refreshToken);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (!jwtAuthenticationManager.isTokenValid(refreshToken, userDetails)) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
                    }
                    return buildTokenResponse(userDetails);
                }

                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.username(), request.password()));
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
                return buildTokenResponse(userDetails);
            } catch (BadCredentialsException ex) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
            }
        });
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
