package com.linkyou.identity.application.query.dto;

import java.util.List;

public record RoleDto(String id, String name, String description, List<String> permissions) {
}
