package com.linkyou.identity.infrastructure.config;

import an.awesome.pipelinr.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingCommandMiddleware implements Command.Middleware {

    private static final Logger log = LoggerFactory.getLogger(LoggingCommandMiddleware.class);

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        long start = System.currentTimeMillis();
        String commandName = command.getClass().getSimpleName();
        log.info("Pipeline start: {}", commandName);

        try {
            R response = next.invoke();
            log.info("Pipeline success: {} completed in {} ms", commandName, System.currentTimeMillis() - start);
            return response;
        } catch (RuntimeException ex) {
            log.warn("Pipeline failed: {} after {} ms - {}", commandName, System.currentTimeMillis() - start, ex.getMessage());
            throw ex;
        }
    }
}
