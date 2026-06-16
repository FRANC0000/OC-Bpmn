package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.document.CreateDocumentDefinitionRequest;
import com.bpmplatform.api.dto.document.CreateDocumentDefinitionResponse;
import com.bpmplatform.api.dto.document.DocumentDefinitionResponse;
import com.bpmplatform.api.dto.document.DocumentInstanceResponse;
import com.bpmplatform.api.dto.document.SubmitDocumentRequest;
import com.bpmplatform.api.dto.document.SubmitDocumentResponse;
import com.bpmplatform.document.application.usecase.CreateDocumentDefinitionUseCase;
import com.bpmplatform.document.application.usecase.SubmitDocumentUseCase;
import com.bpmplatform.document.domain.repository.DocumentDefinitionRepository;
import com.bpmplatform.document.domain.repository.DocumentInstanceRepository;
import com.bpmplatform.infrastructure.multitenant.TenantContext;
import com.bpmplatform.security.infrastructure.auth.BpmUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final CreateDocumentDefinitionUseCase createDefinitionUseCase;
    private final SubmitDocumentUseCase submitDocumentUseCase;
    private final DocumentDefinitionRepository documentDefinitionRepository;
    private final DocumentInstanceRepository documentInstanceRepository;

    public DocumentController(CreateDocumentDefinitionUseCase createDefinitionUseCase,
                              SubmitDocumentUseCase submitDocumentUseCase,
                              DocumentDefinitionRepository documentDefinitionRepository,
                              DocumentInstanceRepository documentInstanceRepository) {
        this.createDefinitionUseCase = createDefinitionUseCase;
        this.submitDocumentUseCase = submitDocumentUseCase;
        this.documentDefinitionRepository = documentDefinitionRepository;
        this.documentInstanceRepository = documentInstanceRepository;
    }

    @GetMapping("/definitions")
    public ResponseEntity<ApiResponse<List<DocumentDefinitionResponse>>> listDefinitions() {
        return withTenantContext(() -> {
            var defs = documentDefinitionRepository.findAll().stream()
                    .map(d -> new DocumentDefinitionResponse(
                            d.getId(), d.getCode(), d.getName(), d.getDescription(),
                            d.getStatus(), 0))
                    .toList();
            return ResponseEntity.ok(ApiResponse.ok("Document definitions retrieved", defs));
        });
    }

    @GetMapping("/definitions/{id}")
    public ResponseEntity<ApiResponse<DocumentDefinitionResponse>> getDefinition(@PathVariable UUID id) {
        return withTenantContext(() -> {
            var d = documentDefinitionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Document definition not found: " + id));
            var response = new DocumentDefinitionResponse(
                    d.getId(), d.getCode(), d.getName(), d.getDescription(),
                    d.getStatus(), 0);
            return ResponseEntity.ok(ApiResponse.ok("Document definition retrieved", response));
        });
    }

    @GetMapping("/instances")
    public ResponseEntity<ApiResponse<List<DocumentInstanceResponse>>> listInstances() {
        return withTenantContext(() -> {
            var instances = documentInstanceRepository.findAll().stream()
                    .map(i -> new DocumentInstanceResponse(
                            i.getId(), i.getFolio(), i.getDocumentId(), i.getVersion(),
                            i.getStatus(), i.getCreatedBy(), i.getCreatedAt()))
                    .toList();
            return ResponseEntity.ok(ApiResponse.ok("Document instances retrieved", instances));
        });
    }

    @PostMapping("/definitions")
    public ResponseEntity<ApiResponse<CreateDocumentDefinitionResponse>> createDefinition(
            @Valid @RequestBody CreateDocumentDefinitionRequest request) {
        return withTenantContext(() -> {
            var input = new CreateDocumentDefinitionUseCase.Input(request.code(), request.name(), request.description());
            var output = createDefinitionUseCase.execute(input);
            var response = new CreateDocumentDefinitionResponse(output.id(), output.code(), output.name());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Document definition created", response));
        });
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<SubmitDocumentResponse>> submit(@Valid @RequestBody SubmitDocumentRequest request) {
        return withTenantContext(() -> {
            var input = new SubmitDocumentUseCase.Input(request.documentCode(), request.valuesJson(), request.createdBy());
            var output = submitDocumentUseCase.execute(input);
            var response = new SubmitDocumentResponse(output.instanceId(), output.folio(), output.status());
            return ResponseEntity.ok(ApiResponse.ok("Document submitted", response));
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
