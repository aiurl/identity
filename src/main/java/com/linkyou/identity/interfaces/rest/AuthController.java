package com.linkyou.identity.interfaces.rest;

import an.awesome.pipelinr.Pipeline;
import com.linkyou.identity.application.command.dto.LoginCommand;
import com.linkyou.identity.application.command.dto.RegisterUserCommand;
import com.linkyou.identity.application.query.dto.AuthTokenView;
import com.linkyou.identity.application.query.dto.UserView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Pipeline pipeline;

    public AuthController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping("/register")
    public Mono<UserView> register(@RequestBody RegisterUserCommand command) {
        return Mono.fromSupplier(() -> pipeline.send(command));
    }

    @PostMapping("/login")
    public Mono<AuthTokenView> login(@RequestBody LoginCommand command) {
        return Mono.fromSupplier(() -> pipeline.send(command));
    }
}
