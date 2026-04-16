package com.linkyou.identity.application.query.dto;

import an.awesome.pipelinr.Command;

import java.util.List;

public record ListRolesQuery() implements Command<List<RoleView>> {
}
