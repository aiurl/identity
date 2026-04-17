package com.linkyou.identity.application.command.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.command.LoginCommand;
import com.linkyou.identity.application.query.dto.AuthTokenDto;
import com.linkyou.identity.application.service.AuthApplicationService;
import org.springframework.stereotype.Component;

@Component
public class LoginCommandHandler implements Command.Handler<LoginCommand, AuthTokenDto> {

    private final AuthApplicationService authApplicationService;

    public LoginCommandHandler(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @Override
    public AuthTokenDto handle(LoginCommand command) {
        return authApplicationService.login(command.username(), command.password());
    }
}
