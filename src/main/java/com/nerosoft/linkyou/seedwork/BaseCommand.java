package com.nerosoft.linkyou.seedwork;

import an.awesome.pipelinr.Command;
import lombok.Getter;

/**
 * 基础命令类
 * 所有命令对象都应该继承自这个基类，以便于统一管理和处理。
 * 
 * @param <R> 命令的返回值类型
 * @author nerosoft
 * @version 1.0
 */
public abstract class BaseCommand<R> implements Command<R> {
    @Getter
    private final String commandId = java.util.UUID.randomUUID().toString();
    @Getter
    private final java.time.Instant issuedAt = java.time.Instant.now();
}