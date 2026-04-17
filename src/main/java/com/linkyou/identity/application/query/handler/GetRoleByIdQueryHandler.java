package com.linkyou.identity.application.query.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.GetRoleByIdQuery;
import com.linkyou.identity.application.query.dto.RoleDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetRoleByIdQueryHandler implements Command.Handler<GetRoleByIdQuery, Optional<RoleDto>> {

    private final GetRoleQueryHandler getRoleQueryHandler;

    public GetRoleByIdQueryHandler(GetRoleQueryHandler getRoleQueryHandler) {
        this.getRoleQueryHandler = getRoleQueryHandler;
    }

    @Override
    public Optional<RoleDto> handle(GetRoleByIdQuery query) {
        return getRoleQueryHandler.findById(query.id());
    }
}
