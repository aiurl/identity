package com.linkyou.identity.application.service;

import com.linkyou.identity.application.query.dto.AuthTokenDto;
import com.linkyou.identity.application.query.dto.UserDto;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public interface AuthApplicationService {

    UserDto register(String username, String nickname, String phone, String email, String password);

    AuthTokenDto login(String username, String password);

    default Mono<UserDto> registerAsync(String username, String nickname, String phone, String email, String password) {
        return Mono.fromCallable(() -> register(username, nickname, phone, email, password))
                .subscribeOn(Schedulers.boundedElastic());
    }

    default Mono<AuthTokenDto> loginAsync(String username, String password) {
        return Mono.fromCallable(() -> login(username, password))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
