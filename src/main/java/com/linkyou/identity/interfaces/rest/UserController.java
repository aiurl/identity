package com.linkyou.identity.interfaces.rest;

import com.linkyou.identity.application.command.dto.CreateUserCommand;
import com.linkyou.identity.application.command.handler.AssignRoleToUserCommandHandler;
import com.linkyou.identity.application.command.handler.CreateUserCommandHandler;
import com.linkyou.identity.application.query.dto.UserView;
import com.linkyou.identity.application.query.handler.GetUserQueryHandler;
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
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserCommandHandler createUserCommandHandler;
    private final AssignRoleToUserCommandHandler assignRoleToUserCommandHandler;
    private final GetUserQueryHandler getUserQueryHandler;

    public UserController(CreateUserCommandHandler createUserCommandHandler,
                          AssignRoleToUserCommandHandler assignRoleToUserCommandHandler,
                          GetUserQueryHandler getUserQueryHandler) {
        this.createUserCommandHandler = createUserCommandHandler;
        this.assignRoleToUserCommandHandler = assignRoleToUserCommandHandler;
        this.getUserQueryHandler = getUserQueryHandler;
    }

    @PostMapping
    public Mono<UserView> create(@RequestBody CreateUserCommand command) {
        return Mono.fromSupplier(() -> createUserCommandHandler.handle(command));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserView>> getById(@PathVariable String id) {
        return Mono.justOrEmpty(getUserQueryHandler.findById(id))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Mono<List<UserView>> list() {
        return Mono.fromSupplier(getUserQueryHandler::findAll);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public Mono<UserView> assignRole(@PathVariable String userId, @PathVariable String roleId) {
        return Mono.fromSupplier(() -> assignRoleToUserCommandHandler.handle(userId, roleId));
    }
}
