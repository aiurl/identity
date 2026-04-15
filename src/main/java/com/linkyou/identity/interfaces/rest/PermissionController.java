package com.linkyou.identity.interfaces.rest;

import com.linkyou.identity.application.command.dto.CreatePermissionCommand;
import com.linkyou.identity.application.command.handler.CreatePermissionCommandHandler;
import com.linkyou.identity.application.query.dto.PermissionView;
import com.linkyou.identity.application.query.handler.GetPermissionQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final CreatePermissionCommandHandler createPermissionCommandHandler;
    private final GetPermissionQueryHandler getPermissionQueryHandler;

    public PermissionController(CreatePermissionCommandHandler createPermissionCommandHandler, GetPermissionQueryHandler getPermissionQueryHandler) {
        this.createPermissionCommandHandler = createPermissionCommandHandler;
        this.getPermissionQueryHandler = getPermissionQueryHandler;
    }

    @PostMapping
    public Mono<PermissionView> create(@RequestBody CreatePermissionCommand command) {
        return Mono.fromSupplier(() -> createPermissionCommandHandler.handle(command));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PermissionView>> getById(@PathVariable String id) {
        return Mono.justOrEmpty(getPermissionQueryHandler.findById(id))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Mono<List<PermissionView>> list() {
        return Mono.fromSupplier(getPermissionQueryHandler::findAll);
    }
}
