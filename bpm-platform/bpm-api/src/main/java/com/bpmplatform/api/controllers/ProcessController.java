package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.process.CreateProcessRequest;
import com.bpmplatform.api.dto.process.CreateProcessResponse;
import com.bpmplatform.api.dto.process.PublishVersionRequest;
import com.bpmplatform.process.application.usecase.CreateProcessDefinitionUseCase;
import com.bpmplatform.process.application.usecase.PublishProcessVersionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/processes")
public class ProcessController {

    private final CreateProcessDefinitionUseCase createProcessUseCase;
    private final PublishProcessVersionUseCase publishVersionUseCase;

    public ProcessController(CreateProcessDefinitionUseCase createProcessUseCase,
                             PublishProcessVersionUseCase publishVersionUseCase) {
        this.createProcessUseCase = createProcessUseCase;
        this.publishVersionUseCase = publishVersionUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreateProcessResponse>> create(@Valid @RequestBody CreateProcessRequest request) {
        var input = new CreateProcessDefinitionUseCase.Input(request.name(), request.description(), request.slug(), request.ownerUserId());
        var output = createProcessUseCase.execute(input);
        var response = new CreateProcessResponse(output.id(), output.name(), output.slug());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Process created", response));
    }

    @PostMapping("/publish-version")
    public ResponseEntity<ApiResponse<Void>> publishVersion(@Valid @RequestBody PublishVersionRequest request) {
        publishVersionUseCase.execute(request.processId(), request.versionId());
        return ResponseEntity.ok(ApiResponse.ok("Version published", null));
    }
}
