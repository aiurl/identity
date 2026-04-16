package com.linkyou.identity.application.query.handler;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.GetPermissionByIdQuery;
import com.linkyou.identity.application.query.dto.PermissionView;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetPermissionByIdQueryHandler implements Command.Handler<GetPermissionByIdQuery, Optional<PermissionView>> {

    private final GetPermissionQueryHandler getPermissionQueryHandler;

    public GetPermissionByIdQueryHandler(GetPermissionQueryHandler getPermissionQueryHandler) {
        this.getPermissionQueryHandler = getPermissionQueryHandler;
    }

    @Override
    public Optional<PermissionView> handle(GetPermissionByIdQuery query) {
        return getPermissionQueryHandler.findById(query.id());
    }
}
