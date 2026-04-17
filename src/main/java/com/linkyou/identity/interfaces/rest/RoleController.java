package com.linkyou.identity.interfaces.rest;

import com.linkyou.identity.application.query.dto.RoleDto;
import com.linkyou.identity.application.service.IdentityApplicationService;
import com.linkyou.identity.interfaces.rest.dto.CreateRoleRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final IdentityApplicationService identityApplicationService;

    public RoleController(IdentityApplicationService identityApplicationService) {
        this.identityApplicationService = identityApplicationService;
    }

    @PostMapping
    public Mono<RoleDto> create(@Valid @RequestBody CreateRoleRequestDto request) {
        return identityApplicationService.createRoleAsync(request.name(), request.description());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<RoleDto>> getById(@PathVariable String id) {
        return identityApplicationService.getRoleByIdAsync(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<RoleDto> list() {
        return identityApplicationService.listRolesAsync();
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    public Mono<RoleDto> assignPermission(@PathVariable String roleId, @PathVariable String permissionId) {
        return identityApplicationService.assignPermissionToRoleAsync(roleId, permissionId);
    }
}
