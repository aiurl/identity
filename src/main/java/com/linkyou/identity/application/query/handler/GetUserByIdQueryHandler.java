package com.linkyou.identity.application.query.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.GetUserByIdQuery;
import com.linkyou.identity.application.query.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetUserByIdQueryHandler implements Command.Handler<GetUserByIdQuery, Optional<UserDto>> {

    private final GetUserQueryHandler getUserQueryHandler;

    public GetUserByIdQueryHandler(GetUserQueryHandler getUserQueryHandler) {
        this.getUserQueryHandler = getUserQueryHandler;
    }

    @Override
    public Optional<UserDto> handle(GetUserByIdQuery query) {
        return getUserQueryHandler.findById(query.id());
    }
}
