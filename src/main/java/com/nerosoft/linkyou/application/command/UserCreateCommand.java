package com.nerosoft.linkyou.application.command;
import com.nerosoft.linkyou.application.dto.UserCreateDto;
import com.nerosoft.linkyou.seedwork.BaseCommand;

import lombok.Getter;


/**
 * Command to create a new user.
 */
public class UserCreateCommand extends BaseCommand<String> {
    @Getter
    private final UserCreateDto data;

    public UserCreateCommand(UserCreateDto data) {
        this.data = data;
    }
}
