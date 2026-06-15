package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.deployment.DeployProcessRequest;
import com.bpmplatform.api.dto.deployment.DeployProcessResponse;
import com.bpmplatform.api.dto.deployment.StartProcessRequest;
import com.bpmplatform.api.dto.deployment.StartProcessResponse;
import com.bpmplatform.process.application.service.ProcessExecutionService;
import com.bpmplatform.process.application.service.ZeebeDeploymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deployment")
public class DeploymentController {

    private final ZeebeDeploymentService deploymentService;
    private final ProcessExecutionService executionService;

    public DeploymentController(ZeebeDeploymentService deploymentService, ProcessExecutionService executionService) {
        this.deploymentService = deploymentService;
        this.executionService = executionService;
    }

    @PostMapping("/deploy")
    public ResponseEntity<ApiResponse<DeployProcessResponse>> deploy(@Valid @RequestBody DeployProcessRequest request) {
        var result = deploymentService.deployVersion(request.processId(), request.versionId());
        var response = new DeployProcessResponse(result.deploymentKey(), result.bpmnProcessId(), result.processVersion());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Process deployed to Zeebe", response));
    }

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<StartProcessResponse>> start(@Valid @RequestBody StartProcessRequest request) {
        var result = executionService.startProcess(request.bpmnProcessId(), request.variables(), request.tenantId());
        var response = new StartProcessResponse(result.processInstanceKey(), result.bpmnProcessId(), result.version());
        return ResponseEntity.ok(ApiResponse.ok("Process instance started", response));
    }
}
