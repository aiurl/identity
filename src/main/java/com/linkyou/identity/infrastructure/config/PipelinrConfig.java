package com.linkyou.identity.infrastructure.config;

import an.awesome.pipelinr.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelinrConfig {

    // @Bean
    // Pipeline pipeline(ObjectProvider<Command.Handler> commandHandlers,
    // ObjectProvider<Command.Middleware> middlewares) {
    // return new Pipelinr()
    // .with(() -> commandHandlers.orderedStream().map(handler -> (Command.Handler)
    // handler))
    // .with(() -> middlewares.orderedStream().map(middleware ->
    // (Command.Middleware) middleware));
    // }
    @Bean
    Pipeline pipeline(ObjectProvider<Command.Handler> commandHandlers,
            ObjectProvider<Notification.Handler> notificationHandlers, ObjectProvider<Command.Middleware> middlewares) {
        return new Pipelinr()
                .with(() -> commandHandlers.stream())
                .with(() -> notificationHandlers.stream())
                .with(() -> middlewares.orderedStream());
    }
}
