package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.process.CreateProcessRequest;
import com.bpmplatform.api.dto.process.CreateProcessResponse;
import com.bpmplatform.api.dto.process.ProcessDefinitionResponse;
import com.bpmplatform.api.dto.process.PublishVersionRequest;
import com.bpmplatform.infrastructure.multitenant.TenantContext;
import com.bpmplatform.process.application.usecase.CreateProcessDefinitionUseCase;
import com.bpmplatform.process.application.usecase.PublishProcessVersionUseCase;
import com.bpmplatform.process.domain.repository.ProcessDefinitionRepository;
import com.bpmplatform.security.infrastructure.auth.BpmUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/processes")
public class ProcessController {

    private final CreateProcessDefinitionUseCase createProcessUseCase;
    private final PublishProcessVersionUseCase publishVersionUseCase;
    private final ProcessDefinitionRepository processDefinitionRepository;

    public ProcessController(CreateProcessDefinitionUseCase createProcessUseCase,
                             PublishProcessVersionUseCase publishVersionUseCase,
                             ProcessDefinitionRepository processDefinitionRepository) {
        this.createProcessUseCase = createProcessUseCase;
        this.publishVersionUseCase = publishVersionUseCase;
        this.processDefinitionRepository = processDefinitionRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProcessDefinitionResponse>>> list() {
        return withTenantContext(() -> {
            var processes = processDefinitionRepository.findAll().stream()
                    .map(p -> new ProcessDefinitionResponse(
                            p.getId(), p.getName(), p.getSlug(), p.getDescription(),
                            p.getStatus(), 0,
                            null))
                    .toList();
            return ResponseEntity.ok(ApiResponse.ok("Processes retrieved", processes));
        });
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProcessDefinitionResponse>> get(@PathVariable UUID id) {
        return withTenantContext(() -> {
            var p = processDefinitionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Process not found: " + id));
            var response = new ProcessDefinitionResponse(
                    p.getId(), p.getName(), p.getSlug(), p.getDescription(),
                    p.getStatus(), 0,
                    null);
            return ResponseEntity.ok(ApiResponse.ok("Process retrieved", response));
        });
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreateProcessResponse>> create(@Valid @RequestBody CreateProcessRequest request) {
        return withTenantContext(() -> {
            var input = new CreateProcessDefinitionUseCase.Input(request.name(), request.description(), request.slug(), request.ownerUserId());
            var output = createProcessUseCase.execute(input);
            var response = new CreateProcessResponse(output.id(), output.name(), output.slug());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Process created", response));
        });
    }

    @PostMapping("/publish-version")
    public ResponseEntity<ApiResponse<Void>> publishVersion(@Valid @RequestBody PublishVersionRequest request) {
        return withTenantContext(() -> {
            publishVersionUseCase.execute(request.processId(), request.versionId());
            return ResponseEntity.ok(ApiResponse.ok("Version published", null));
        });
    }

    @PutMapping("/{id}/bpmn")
    public ResponseEntity<ApiResponse<Void>> updateBpmn(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        return withTenantContext(() -> {
            var process = processDefinitionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Process not found: " + id));
            process.setBpmnXml(body.get("bpmnXml"));
            processDefinitionRepository.save(process);
            return ResponseEntity.ok(ApiResponse.ok("BPMN updated", null));
        });
    }

    private <T> ResponseEntity<T> withTenantContext(java.util.function.Supplier<ResponseEntity<T>> block) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof BpmUserDetails user) {
            var schema = "tenant_" + user.getTenantId().toString().replace("-", "");
            TenantContext.setTenantId(schema);
        }
        try {
            return block.get();
        } finally {
            TenantContext.clear();
        }
    }
}
