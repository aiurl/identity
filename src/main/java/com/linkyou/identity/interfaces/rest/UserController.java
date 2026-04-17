package com.linkyou.identity.interfaces.rest;

import com.linkyou.identity.application.query.dto.UserDto;
import com.linkyou.identity.application.service.IdentityApplicationService;
import com.linkyou.identity.interfaces.rest.dto.CreateUserRequestDto;
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
@RequestMapping("/api/users")
public class UserController {

    private final IdentityApplicationService identityApplicationService;

    public UserController(IdentityApplicationService identityApplicationService) {
        this.identityApplicationService = identityApplicationService;
    }

    @PostMapping
    public Mono<UserDto> create(@Valid @RequestBody CreateUserRequestDto request) {
        return identityApplicationService.createUserAsync(
                request.username(),
                request.nickname(),
                request.phone(),
                request.email()
        );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> getById(@PathVariable String id) {
        return identityApplicationService.getUserByIdAsync(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<UserDto> list() {
        return identityApplicationService.listUsersAsync();
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public Mono<UserDto> assignRole(@PathVariable String userId, @PathVariable String roleId) {
        return identityApplicationService.assignRoleToUserAsync(userId, roleId);
    }
}
