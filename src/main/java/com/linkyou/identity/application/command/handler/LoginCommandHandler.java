package com.linkyou.identity.application.command.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.command.dto.LoginCommand;
import com.linkyou.identity.application.query.dto.AuthTokenView;
import com.linkyou.identity.application.service.AuthApplicationService;
import org.springframework.stereotype.Component;

@Component
public class LoginCommandHandler implements Command.Handler<LoginCommand, AuthTokenView> {

    private final AuthApplicationService authApplicationService;

    public LoginCommandHandler(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @Override
    public AuthTokenView handle(LoginCommand command) {
        return authApplicationService.login(command);
    }
}
