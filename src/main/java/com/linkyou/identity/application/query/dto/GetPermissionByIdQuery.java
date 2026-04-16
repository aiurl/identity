package com.linkyou.identity.application.query.dto;

import an.awesome.pipelinr.Command;

import java.util.Optional;

public record GetPermissionByIdQuery(String id) implements Command<Optional<PermissionView>> {
}
