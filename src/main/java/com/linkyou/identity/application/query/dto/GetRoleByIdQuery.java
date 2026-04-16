package com.linkyou.identity.application.query.dto;

import an.awesome.pipelinr.Command;

import java.util.Optional;

public record GetRoleByIdQuery(String id) implements Command<Optional<RoleView>> {
}
