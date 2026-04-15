package com.linkyou.identity.application.command.dto;

public record CreateUserCommand(String username, String nickname, String phone, String email) {
}
