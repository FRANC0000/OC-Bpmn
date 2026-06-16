package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.document.AddBlocksRequest;
import com.bpmplatform.api.dto.document.AddBlocksResponse;
import com.bpmplatform.api.dto.document.AddCatalogItemRequest;
import com.bpmplatform.api.dto.document.AddCatalogItemResponse;
import com.bpmplatform.api.dto.document.CreateBlockCatalogRequest;
import com.bpmplatform.api.dto.document.CreateBlockCatalogResponse;
import com.bpmplatform.api.dto.document.CreateCatalogRequest;
import com.bpmplatform.api.dto.document.CreateCatalogResponse;
import com.bpmplatform.api.dto.document.CreateMetadataSchemaRequest;
import com.bpmplatform.api.dto.document.CreateMetadataSchemaResponse;
import com.bpmplatform.api.dto.document.SaveDraftRequest;
import com.bpmplatform.api.dto.document.SaveDraftResponse;
import com.bpmplatform.document.application.usecase.AddBlocksToVersionUseCase;
import com.bpmplatform.document.application.usecase.AddCatalogItemUseCase;
import com.bpmplatform.document.application.usecase.CreateBlockCatalogUseCase;
import com.bpmplatform.document.application.usecase.CreateCatalogUseCase;
import com.bpmplatform.document.application.usecase.CreateMetadataSchemaUseCase;
import com.bpmplatform.document.application.usecase.SaveDraftUseCase;
import com.bpmplatform.infrastructure.multitenant.TenantContext;
import com.bpmplatform.security.infrastructure.auth.BpmUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentEngineController {

    private final CreateBlockCatalogUseCase createBlockCatalogUseCase;
    private final AddBlocksToVersionUseCase addBlocksToVersionUseCase;
    private final CreateMetadataSchemaUseCase createMetadataSchemaUseCase;
    private final SaveDraftUseCase saveDraftUseCase;
    private final CreateCatalogUseCase createCatalogUseCase;
    private final AddCatalogItemUseCase addCatalogItemUseCase;

    public DocumentEngineController(CreateBlockCatalogUseCase createBlockCatalogUseCase,
                                    AddBlocksToVersionUseCase addBlocksToVersionUseCase,
                                    CreateMetadataSchemaUseCase createMetadataSchemaUseCase,
                                    SaveDraftUseCase saveDraftUseCase,
                                    CreateCatalogUseCase createCatalogUseCase,
                                    AddCatalogItemUseCase addCatalogItemUseCase) {
        this.createBlockCatalogUseCase = createBlockCatalogUseCase;
        this.addBlocksToVersionUseCase = addBlocksToVersionUseCase;
        this.createMetadataSchemaUseCase = createMetadataSchemaUseCase;
        this.saveDraftUseCase = saveDraftUseCase;
        this.createCatalogUseCase = createCatalogUseCase;
        this.addCatalogItemUseCase = addCatalogItemUseCase;
    }

    @PostMapping("/block-catalogs")
    public ResponseEntity<ApiResponse<CreateBlockCatalogResponse>> createBlockCatalog(
            @Valid @RequestBody CreateBlockCatalogRequest request) {
        return withTenantContext(() -> {
            var input = new CreateBlockCatalogUseCase.Input(request.code(), request.name(), request.description());
            var output = createBlockCatalogUseCase.execute(input);
            var response = new CreateBlockCatalogResponse(output.id(), output.code(), output.name());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Block catalog created", response));
        });
    }

    @PostMapping("/versions/blocks")
    public ResponseEntity<ApiResponse<AddBlocksResponse>> addBlocks(@Valid @RequestBody AddBlocksRequest request) {
        return withTenantContext(() -> {
            var input = new AddBlocksToVersionUseCase.Input(request.documentId(), request.versionId(), request.blocksJson());
            var output = addBlocksToVersionUseCase.execute(input);
            var response = new AddBlocksResponse(output.versionId(), output.version());
            return ResponseEntity.ok(ApiResponse.ok("Blocks added to version", response));
        });
    }

    @PostMapping("/metadata-schemas")
    public ResponseEntity<ApiResponse<CreateMetadataSchemaResponse>> createMetadataSchema(
            @Valid @RequestBody CreateMetadataSchemaRequest request) {
        return withTenantContext(() -> {
            var input = new CreateMetadataSchemaUseCase.Input(request.code(), request.name(), request.description(), request.schemaJson());
            var output = createMetadataSchemaUseCase.execute(input);
            var response = new CreateMetadataSchemaResponse(output.id(), output.code(), output.name());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Metadata schema created", response));
        });
    }

    @PostMapping("/drafts")
    public ResponseEntity<ApiResponse<SaveDraftResponse>> saveDraft(@Valid @RequestBody SaveDraftRequest request) {
        return withTenantContext(() -> {
            var input = new SaveDraftUseCase.Input(request.instanceId(), request.valuesJson());
            var output = saveDraftUseCase.execute(input);
            var response = new SaveDraftResponse(output.instanceId(), output.folio(), output.status());
            return ResponseEntity.ok(ApiResponse.ok("Draft saved", response));
        });
    }

    @PostMapping("/catalogs")
    public ResponseEntity<ApiResponse<CreateCatalogResponse>> createCatalog(@Valid @RequestBody CreateCatalogRequest request) {
        return withTenantContext(() -> {
            var input = new CreateCatalogUseCase.Input(request.code(), request.name(), request.description());
            var output = createCatalogUseCase.execute(input);
            var response = new CreateCatalogResponse(output.id(), output.code(), output.name());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Catalog created", response));
        });
    }

    @PostMapping("/catalog-items")
    public ResponseEntity<ApiResponse<AddCatalogItemResponse>> addCatalogItem(@Valid @RequestBody AddCatalogItemRequest request) {
        return withTenantContext(() -> {
            var input = new AddCatalogItemUseCase.Input(request.catalogId(), request.code(), request.label(), request.sortOrder(), request.metadataJson());
            var output = addCatalogItemUseCase.execute(input);
            var response = new AddCatalogItemResponse(output.id(), output.code(), output.label());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Catalog item added", response));
        });
    }

    private <T> ResponseEntity<T> withTenantContext(Supplier<ResponseEntity<T>> block) {
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
