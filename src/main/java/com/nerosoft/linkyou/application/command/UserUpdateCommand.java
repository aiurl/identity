package com.nerosoft.linkyou.application.command;

import com.nerosoft.linkyou.seedwork.BaseCommand;
import lombok.Getter;

/**
 * 用户更新命令
 */
@Getter
public class UserUpdateCommand extends BaseCommand<Void> {

    private final String id;
    private final java.util.HashMap<String, Object> data;

    public UserUpdateCommand(String id, java.util.HashMap<String, Object> data) {
        this.id = id;
        this.data = data;
    }

}
