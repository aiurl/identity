package com.linkyou.identity.application.query.dto;

import an.awesome.pipelinr.Command;

import java.util.List;

public record ListPermissionsQuery() implements Command<List<PermissionView>> {
}
