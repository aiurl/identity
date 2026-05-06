package com.nerosoft.linkyou.application.command;

import com.nerosoft.linkyou.seedwork.BaseCommand;
import lombok.Getter;

/**
 * Command to change user password.
 */
@Getter
public class UserPasswordChangeCommand extends BaseCommand<Void> {
    private final String id;
    private final String password;
    private final String action;

    /**
     * Initialize a new instance of UserPasswordChangeCommand.
     * @param id The ID of the user.
     * @param password The new password to be set for the user.
     * @param action The action to be performed, e.g., "change" or "reset".
     */
    public UserPasswordChangeCommand(String id, String password, String action) {
        this.id = id;
        this.password = password;
        this.action = action;
    }
}
