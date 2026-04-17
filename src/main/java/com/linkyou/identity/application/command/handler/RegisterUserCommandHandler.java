package com.linkyou.identity.application.command.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.command.RegisterUserCommand;
import com.linkyou.identity.application.query.dto.UserDto;
import com.linkyou.identity.application.service.AuthApplicationService;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserCommandHandler implements Command.Handler<RegisterUserCommand, UserDto> {

    private final AuthApplicationService authApplicationService;

    public RegisterUserCommandHandler(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @Override
    public UserDto handle(RegisterUserCommand command) {
        return authApplicationService.register(
                command.username(),
                command.nickname(),
                command.phone(),
                command.email(),
                command.password()
        );
    }
}
