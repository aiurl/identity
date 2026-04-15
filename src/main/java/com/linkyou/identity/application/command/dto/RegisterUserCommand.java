package com.linkyou.identity.application.command.dto;

public record RegisterUserCommand(String username, String nickname, String phone, String email, String password) {
}
