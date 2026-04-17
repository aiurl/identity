package com.linkyou.identity.application.command;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.application.query.dto.AuthTokenDto;
import jakarta.validation.constraints.NotBlank;

public record LoginCommand(
        @NotBlank(message = "username不能为空") String username,
        @NotBlank(message = "password不能为空") String password
) implements Command<AuthTokenDto> {
}
