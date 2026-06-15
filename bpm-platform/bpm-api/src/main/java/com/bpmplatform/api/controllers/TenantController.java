package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.tenant.ChangePlanRequest;
import com.bpmplatform.api.dto.tenant.RegisterTenantRequest;
import com.bpmplatform.api.dto.tenant.RegisterTenantResponse;
import com.bpmplatform.tenant.application.usecase.ChangeTenantPlanUseCase;
import com.bpmplatform.tenant.application.usecase.RegisterTenantUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {

    private final RegisterTenantUseCase registerTenantUseCase;
    private final ChangeTenantPlanUseCase changeTenantPlanUseCase;

    public TenantController(RegisterTenantUseCase registerTenantUseCase, ChangeTenantPlanUseCase changeTenantPlanUseCase) {
        this.registerTenantUseCase = registerTenantUseCase;
        this.changeTenantPlanUseCase = changeTenantPlanUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RegisterTenantResponse>> register(@Valid @RequestBody RegisterTenantRequest request) {
        var input = new RegisterTenantUseCase.Input(request.name(), request.slug(), request.planCode());
        var output = registerTenantUseCase.execute(input);
        var response = new RegisterTenantResponse(output.tenantId(), output.slug(), output.schemaName(), output.plan());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Tenant registered", response));
    }

    @PostMapping("/{id}/change-plan")
    public ResponseEntity<ApiResponse<Void>> changePlan(@PathVariable UUID id, @Valid @RequestBody ChangePlanRequest request) {
        changeTenantPlanUseCase.execute(id, request.planCode());
        return ResponseEntity.ok(ApiResponse.ok("Plan changed", null));
    }
}
