package com.linkyou.identity.interfaces.rest;

import com.linkyou.identity.application.query.dto.PermissionDto;
import com.linkyou.identity.application.service.IdentityApplicationService;
import com.linkyou.identity.interfaces.rest.dto.CreatePermissionRequestDto;
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
@RequestMapping("/api/permissions")
public class PermissionController {

    private final IdentityApplicationService identityApplicationService;

    public PermissionController(IdentityApplicationService identityApplicationService) {
        this.identityApplicationService = identityApplicationService;
    }

    @PostMapping
    public Mono<PermissionDto> create(@Valid @RequestBody CreatePermissionRequestDto request) {
        return identityApplicationService.createPermissionAsync(request.code(), request.description());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PermissionDto>> getById(@PathVariable String id) {
        return identityApplicationService.getPermissionByIdAsync(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<PermissionDto> list() {
        return identityApplicationService.listPermissionsAsync();
    }
}
