package com.nerosoft.linkyou.facade.handler;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.nerosoft.linkyou.domain.event.UserPasswordChangedEvent;

import an.awesome.pipelinr.Pipeline;

@Component
public class LoggingServiceHandler {
    private final Pipeline pipeline;

    public LoggingServiceHandler(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Async
    @EventListener
    public void handleUserPasswordChangedEvent(UserPasswordChangedEvent event) {
        // var command = new OperationLogCreateCommand(
        //         "用户密码变更",
        //         "用户ID: " + event.getUserId() + ", 变更类型: " + event.getChangeType() + ", 变更时间: " + event.getChangedAt(),
        //         java.time.Instant.now()
        // );
        // pipeline.send(command);
    }
}
