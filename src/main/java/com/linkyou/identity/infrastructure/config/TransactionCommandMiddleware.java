package com.linkyou.identity.infrastructure.config;

import an.awesome.pipelinr.Command;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class TransactionCommandMiddleware implements Command.Middleware {

    private final TransactionTemplate transactionTemplate;

    public TransactionCommandMiddleware(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        if (command.getClass().getSimpleName().endsWith("Query")) {
            return next.invoke();
        }

        return transactionTemplate.execute(status -> next.invoke());
    }
}
