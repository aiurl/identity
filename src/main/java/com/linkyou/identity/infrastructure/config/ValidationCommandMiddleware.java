package com.linkyou.identity.infrastructure.config;

import an.awesome.pipelinr.Command;
import com.linkyou.identity.common.exception.DomainException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class ValidationCommandMiddleware implements Command.Middleware {

    private final Validator validator;

    public ValidationCommandMiddleware(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        Set<ConstraintViolation<C>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            throw new DomainException(message);
        }
        return next.invoke();
    }
}
