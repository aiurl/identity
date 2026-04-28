package com.nerosoft.linkyou.seedwork;

import an.awesome.pipelinr.Command;

/**
 * 基础命令类
 * 
 * 所有命令对象都应该继承自这个基类，以便于统一管理和处理。
 * 
 * @author nerosoft
 * @version 1.0
 */
public abstract class BaseCommand implements Command<Void> {
    private final String commandId = java.util.UUID.randomUUID().toString();
    private final java.time.Instant issuedAt = java.time.Instant.now();

    /**
     * 获取命令的唯一标识
     * @return 命令的唯一标识
     */
    public String getCommandId() {
        return commandId;
    }

    /**
     * 获取命令的发出时间
     * @return 命令的发出时间
     */
    public java.time.Instant getIssuedAt() {
        return issuedAt;
    }
}
