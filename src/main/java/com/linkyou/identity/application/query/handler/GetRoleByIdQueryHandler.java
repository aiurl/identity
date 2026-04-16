package com.linkyou.identity.application.query.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.GetRoleByIdQuery;
import com.linkyou.identity.application.query.dto.RoleView;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetRoleByIdQueryHandler implements Command.Handler<GetRoleByIdQuery, Optional<RoleView>> {

    private final GetRoleQueryHandler getRoleQueryHandler;

    public GetRoleByIdQueryHandler(GetRoleQueryHandler getRoleQueryHandler) {
        this.getRoleQueryHandler = getRoleQueryHandler;
    }

    @Override
    public Optional<RoleView> handle(GetRoleByIdQuery query) {
        return getRoleQueryHandler.findById(query.id());
    }
}
