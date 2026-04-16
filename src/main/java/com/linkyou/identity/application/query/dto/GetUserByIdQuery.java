package com.linkyou.identity.application.query.dto;

import an.awesome.pipelinr.Command;

import java.util.Optional;

public record GetUserByIdQuery(String id) implements Command<Optional<UserView>> {
}
