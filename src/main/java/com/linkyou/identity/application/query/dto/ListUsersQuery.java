package com.linkyou.identity.application.query.dto;

import an.awesome.pipelinr.Command;

import java.util.List;

public record ListUsersQuery() implements Command<List<UserView>> {
}
