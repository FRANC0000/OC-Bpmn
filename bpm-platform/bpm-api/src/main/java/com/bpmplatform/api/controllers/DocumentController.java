package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.document.CreateDocumentDefinitionRequest;
import com.bpmplatform.api.dto.document.CreateDocumentDefinitionResponse;
import com.bpmplatform.api.dto.document.SubmitDocumentRequest;
import com.bpmplatform.api.dto.document.SubmitDocumentResponse;
import com.bpmplatform.document.application.usecase.CreateDocumentDefinitionUseCase;
import com.bpmplatform.document.application.usecase.SubmitDocumentUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final CreateDocumentDefinitionUseCase createDefinitionUseCase;
    private final SubmitDocumentUseCase submitDocumentUseCase;

    public DocumentController(CreateDocumentDefinitionUseCase createDefinitionUseCase,
                              SubmitDocumentUseCase submitDocumentUseCase) {
        this.createDefinitionUseCase = createDefinitionUseCase;
        this.submitDocumentUseCase = submitDocumentUseCase;
    }

    @PostMapping("/definitions")
    public ResponseEntity<ApiResponse<CreateDocumentDefinitionResponse>> createDefinition(
            @Valid @RequestBody CreateDocumentDefinitionRequest request) {
        var input = new CreateDocumentDefinitionUseCase.Input(request.code(), request.name(), request.description());
        var output = createDefinitionUseCase.execute(input);
        var response = new CreateDocumentDefinitionResponse(output.id(), output.code(), output.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Document definition created", response));
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<SubmitDocumentResponse>> submit(@Valid @RequestBody SubmitDocumentRequest request) {
        var input = new SubmitDocumentUseCase.Input(request.documentCode(), request.valuesJson(), request.createdBy());
        var output = submitDocumentUseCase.execute(input);
        var response = new SubmitDocumentResponse(output.instanceId(), output.folio(), output.status());
        return ResponseEntity.ok(ApiResponse.ok("Document submitted", response));
    }
}
