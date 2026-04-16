package com.linkyou.identity.interfaces.rest;

import an.awesome.pipelinr.Pipeline;
import com.linkyou.identity.application.command.dto.AssignPermissionToRoleCommand;
import com.linkyou.identity.application.command.dto.CreateRoleCommand;
import com.linkyou.identity.application.query.dto.GetRoleByIdQuery;
import com.linkyou.identity.application.query.dto.ListRolesQuery;
import com.linkyou.identity.application.query.dto.RoleView;
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
@RequestMapping("/api/roles")
public class RoleController {

    private final Pipeline pipeline;

    public RoleController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public Mono<RoleView> create(@RequestBody CreateRoleCommand command) {
        return Mono.fromSupplier(() -> pipeline.send(command));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<RoleView>> getById(@PathVariable String id) {
        return Mono.justOrEmpty(pipeline.send(new GetRoleByIdQuery(id)))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Mono<List<RoleView>> list() {
        return Mono.fromSupplier(() -> pipeline.send(new ListRolesQuery()));
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    public Mono<RoleView> assignPermission(@PathVariable String roleId, @PathVariable String permissionId) {
        return Mono.fromSupplier(() -> pipeline.send(new AssignPermissionToRoleCommand(roleId, permissionId)));
    }
}
