package com.linkyou.identity.application.command.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.command.dto.RegisterUserCommand;
import com.linkyou.identity.application.query.dto.UserView;
import com.linkyou.identity.application.service.AuthApplicationService;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserCommandHandler implements Command.Handler<RegisterUserCommand, UserView> {

    private final AuthApplicationService authApplicationService;

    public RegisterUserCommandHandler(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @Override
    public UserView handle(RegisterUserCommand command) {
        return authApplicationService.register(command);
    }
}
