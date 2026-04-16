package com.linkyou.identity.application.query.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.GetUserByIdQuery;
import com.linkyou.identity.application.query.dto.UserView;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetUserByIdQueryHandler implements Command.Handler<GetUserByIdQuery, Optional<UserView>> {

    private final GetUserQueryHandler getUserQueryHandler;

    public GetUserByIdQueryHandler(GetUserQueryHandler getUserQueryHandler) {
        this.getUserQueryHandler = getUserQueryHandler;
    }

    @Override
    public Optional<UserView> handle(GetUserByIdQuery query) {
        return getUserQueryHandler.findById(query.id());
    }
}
