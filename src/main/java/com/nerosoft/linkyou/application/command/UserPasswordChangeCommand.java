package com.nerosoft.linkyou.application.command;

import com.nerosoft.linkyou.seedwork.BaseCommand;
import lombok.Getter;

/**
 * 用户密码修改命令
 */
@Getter
public class UserPasswordChangeCommand extends BaseCommand<Void> {
    private final String id;
    private final String oldPassword;
    private final String newPassword;

    public UserPasswordChangeCommand(String id, String oldPassword, String newPassword) {
        this.id = id;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
