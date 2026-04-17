package com.linkyou.identity.application.query.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.GetPermissionByIdQuery;
import com.linkyou.identity.application.query.dto.PermissionDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetPermissionByIdQueryHandler implements Command.Handler<GetPermissionByIdQuery, Optional<PermissionDto>> {

    private final GetPermissionQueryHandler getPermissionQueryHandler;

    public GetPermissionByIdQueryHandler(GetPermissionQueryHandler getPermissionQueryHandler) {
        this.getPermissionQueryHandler = getPermissionQueryHandler;
    }

    @Override
    public Optional<PermissionDto> handle(GetPermissionByIdQuery query) {
        return getPermissionQueryHandler.findById(query.id());
    }
}
