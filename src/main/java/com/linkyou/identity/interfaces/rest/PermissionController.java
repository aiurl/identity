package com.linkyou.identity.interfaces.rest;

import an.awesome.pipelinr.Pipeline;
import com.linkyou.identity.application.command.dto.CreatePermissionCommand;
import com.linkyou.identity.application.query.dto.GetPermissionByIdQuery;
import com.linkyou.identity.application.query.dto.ListPermissionsQuery;
import com.linkyou.identity.application.query.dto.PermissionView;
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

    private final Pipeline pipeline;

    public PermissionController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public Mono<PermissionView> create(@RequestBody CreatePermissionCommand command) {
        return Mono.fromSupplier(() -> pipeline.send(command));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PermissionView>> getById(@PathVariable String id) {
        return Mono.justOrEmpty(pipeline.send(new GetPermissionByIdQuery(id)))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Mono<List<PermissionView>> list() {
        return Mono.fromSupplier(() -> pipeline.send(new ListPermissionsQuery()));
    }
}
