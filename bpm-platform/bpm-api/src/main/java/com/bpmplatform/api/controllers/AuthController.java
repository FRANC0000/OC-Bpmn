package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.auth.LoginRequest;
import com.bpmplatform.api.dto.auth.LoginResponse;
import com.bpmplatform.api.dto.auth.RegisterRequest;
import com.bpmplatform.api.dto.auth.RegisterResponse;
import com.bpmplatform.infrastructure.multitenant.TenantContext;
import com.bpmplatform.security.application.usecase.RegisterUserUseCase;
import com.bpmplatform.security.application.usecase.LoginUseCase;
import com.bpmplatform.tenant.application.usecase.RegisterTenantUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterTenantUseCase registerTenantUseCase;
    private final RegisterUserUseCase registerUserUseCase;

    public AuthController(LoginUseCase loginUseCase,
                          RegisterTenantUseCase registerTenantUseCase,
                          RegisterUserUseCase registerUserUseCase) {
        this.loginUseCase = loginUseCase;
        this.registerTenantUseCase = registerTenantUseCase;
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        if (request.tenantId() != null) {
            TenantContext.setTenantId("tenant_" + request.tenantId().toString().replace("-", ""));
        }
        try {
            var input = new LoginUseCase.Input(request.email(), request.password());
            var output = loginUseCase.execute(input);
            var response = new LoginResponse(output.token(), output.userId(), output.email(), output.displayName(), output.role());
            return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
        } finally {
            if (request.tenantId() != null) {
                TenantContext.clear();
            }
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        String slug = request.email().replaceAll("[^a-z0-9]", "-").replaceAll("-+", "-").replaceAll("^-|-$", "");

        var tenantInput = new RegisterTenantUseCase.Input(request.displayName(), slug, "basic");
        var tenantOutput = registerTenantUseCase.execute(tenantInput);

        TenantContext.setTenantId(tenantOutput.schemaName());
        try {
            var userInput = new RegisterUserUseCase.Input(
                    request.email(), request.password(), request.displayName(), tenantOutput.tenantId());
            var userOutput = registerUserUseCase.execute(userInput);

            var response = new RegisterResponse(
                    userOutput.userId(), tenantOutput.tenantId(), userOutput.email(), userOutput.displayName());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("User registered", response));
        } finally {
            TenantContext.clear();
        }
    }
}
