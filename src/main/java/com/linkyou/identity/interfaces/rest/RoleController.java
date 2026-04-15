package com.linkyou.identity.interfaces.rest;

import com.linkyou.identity.application.command.dto.CreateRoleCommand;
import com.linkyou.identity.application.command.handler.AssignPermissionToRoleCommandHandler;
import com.linkyou.identity.application.command.handler.CreateRoleCommandHandler;
import com.linkyou.identity.application.query.dto.RoleView;
import com.linkyou.identity.application.query.handler.GetRoleQueryHandler;
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

    private final CreateRoleCommandHandler createRoleCommandHandler;
    private final AssignPermissionToRoleCommandHandler assignPermissionToRoleCommandHandler;
    private final GetRoleQueryHandler getRoleQueryHandler;

    public RoleController(CreateRoleCommandHandler createRoleCommandHandler,
                          AssignPermissionToRoleCommandHandler assignPermissionToRoleCommandHandler,
                          GetRoleQueryHandler getRoleQueryHandler) {
        this.createRoleCommandHandler = createRoleCommandHandler;
        this.assignPermissionToRoleCommandHandler = assignPermissionToRoleCommandHandler;
        this.getRoleQueryHandler = getRoleQueryHandler;
    }

    @PostMapping
    public Mono<RoleView> create(@RequestBody CreateRoleCommand command) {
        return Mono.fromSupplier(() -> createRoleCommandHandler.handle(command));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<RoleView>> getById(@PathVariable String id) {
        return Mono.justOrEmpty(getRoleQueryHandler.findById(id))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Mono<List<RoleView>> list() {
        return Mono.fromSupplier(getRoleQueryHandler::findAll);
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    public Mono<RoleView> assignPermission(@PathVariable String roleId, @PathVariable String permissionId) {
        return Mono.fromSupplier(() -> assignPermissionToRoleCommandHandler.handle(roleId, permissionId));
    }
}
