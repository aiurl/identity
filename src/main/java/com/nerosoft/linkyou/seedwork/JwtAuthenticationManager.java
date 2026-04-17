package com.nerosoft.linkyou.seedwork;

import java.util.List;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import reactor.core.publisher.Mono;

public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        
        var username = authentication.getPrincipal().toString();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return Mono.just(new UsernamePasswordAuthenticationToken(username, authentication.getCredentials(), authorities));
    }
    
}
