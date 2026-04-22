package com.nerosoft.linkyou.facade.handler;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.nerosoft.linkyou.domain.event.UserPasswordChangedEvent;

@Component
public final class MessageServiceHandler {

    @Async
    @EventListener
    public void handleUserPasswordChangedEvent(UserPasswordChangedEvent event) {
        // 在这里处理用户密码变更事件，例如发送通知邮件或短信
        System.out.println("用户密码已变更，用户ID: " + event.getUserId() + ", 变更类型: " + event.getChangeType() + ", 变更时间: "
                + event.getChangedAt());
    }
}
