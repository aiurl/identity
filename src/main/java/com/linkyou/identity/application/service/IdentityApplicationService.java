package com.linkyou.identity.application.service;

import com.linkyou.identity.application.query.dto.PermissionDto;
import com.linkyou.identity.application.query.dto.RoleDto;
import com.linkyou.identity.application.query.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;

public interface IdentityApplicationService {

    UserDto createUser(String username, String nickname, String phone, String email);

    Optional<UserDto> getUserById(String id);

    List<UserDto> listUsers();

    UserDto assignRoleToUser(String userId, String roleId);

    RoleDto createRole(String name, String description);

    Optional<RoleDto> getRoleById(String id);

    List<RoleDto> listRoles();

    RoleDto assignPermissionToRole(String roleId, String permissionId);

    PermissionDto createPermission(String code, String description);

    Optional<PermissionDto> getPermissionById(String id);

    List<PermissionDto> listPermissions();

    default Mono<UserDto> createUserAsync(String username, String nickname, String phone, String email) {
        return Mono.fromCallable(() -> createUser(username, nickname, phone, email))
                .subscribeOn(Schedulers.boundedElastic());
    }

    default Mono<UserDto> getUserByIdAsync(String id) {
        return Mono.fromCallable(() -> getUserById(id))
                .flatMap(Mono::justOrEmpty)
                .subscribeOn(Schedulers.boundedElastic());
    }

    default Flux<UserDto> listUsersAsync() {
        return Flux.defer(() -> Flux.fromIterable(listUsers()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    default Mono<UserDto> assignRoleToUserAsync(String userId, String roleId) {
        return Mono.fromCallable(() -> assignRoleToUser(userId, roleId))
                .subscribeOn(Schedulers.boundedElastic());
    }

    default Mono<RoleDto> createRoleAsync(String name, String description) {
        return Mono.fromCallable(() -> createRole(name, description))
                .subscribeOn(Schedulers.boundedElastic());
    }

    default Mono<RoleDto> getRoleByIdAsync(String id) {
        return Mono.fromCallable(() -> getRoleById(id))
                .flatMap(Mono::justOrEmpty)
                .subscribeOn(Schedulers.boundedElastic());
    }

    default Flux<RoleDto> listRolesAsync() {
        return Flux.defer(() -> Flux.fromIterable(listRoles()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    default Mono<RoleDto> assignPermissionToRoleAsync(String roleId, String permissionId) {
        return Mono.fromCallable(() -> assignPermissionToRole(roleId, permissionId))
                .subscribeOn(Schedulers.boundedElastic());
    }

    default Mono<PermissionDto> createPermissionAsync(String code, String description) {
        return Mono.fromCallable(() -> createPermission(code, description))
                .subscribeOn(Schedulers.boundedElastic());
    }

    default Mono<PermissionDto> getPermissionByIdAsync(String id) {
        return Mono.fromCallable(() -> getPermissionById(id))
                .flatMap(Mono::justOrEmpty)
                .subscribeOn(Schedulers.boundedElastic());
    }

    default Flux<PermissionDto> listPermissionsAsync() {
        return Flux.defer(() -> Flux.fromIterable(listPermissions()))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
