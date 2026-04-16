package com.linkyou.identity.interfaces.rest;

import an.awesome.pipelinr.Pipeline;
import com.linkyou.identity.application.command.dto.AssignRoleToUserCommand;
import com.linkyou.identity.application.command.dto.CreateUserCommand;
import com.linkyou.identity.application.query.dto.GetUserByIdQuery;
import com.linkyou.identity.application.query.dto.ListUsersQuery;
import com.linkyou.identity.application.query.dto.UserView;
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

    private final Pipeline pipeline;

    public UserController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public Mono<UserView> create(@RequestBody CreateUserCommand command) {
        return Mono.fromSupplier(() -> pipeline.send(command));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserView>> getById(@PathVariable String id) {
        return Mono.justOrEmpty(pipeline.send(new GetUserByIdQuery(id)))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Mono<List<UserView>> list() {
        return Mono.fromSupplier(() -> pipeline.send(new ListUsersQuery()));
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public Mono<UserView> assignRole(@PathVariable String userId, @PathVariable String roleId) {
        return Mono.fromSupplier(() -> pipeline.send(new AssignRoleToUserCommand(userId, roleId)));
    }
}
