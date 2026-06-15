package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.security.AssignRoleRequest;
import com.bpmplatform.api.dto.security.RegisterUserRequest;
import com.bpmplatform.api.dto.security.RegisterUserResponse;
import com.bpmplatform.security.application.usecase.AssignRoleUseCase;
import com.bpmplatform.security.application.usecase.RegisterUserUseCase;
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
@RequestMapping("/api/v1/security")
public class SecurityController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AssignRoleUseCase assignRoleUseCase;

    public SecurityController(RegisterUserUseCase registerUserUseCase, AssignRoleUseCase assignRoleUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.assignRoleUseCase = assignRoleUseCase;
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<RegisterUserResponse>> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        var input = new RegisterUserUseCase.Input(request.email(), request.password(), request.displayName(), request.tenantId());
        var output = registerUserUseCase.execute(input);
        var response = new RegisterUserResponse(output.userId(), output.email(), output.displayName());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("User registered", response));
    }

    @PostMapping("/roles/primary")
    public ResponseEntity<ApiResponse<Void>> assignPrimaryRole(@Valid @RequestBody AssignRoleRequest request) {
        assignRoleUseCase.assignPrimary(request.userId(), request.roleId());
        return ResponseEntity.ok(ApiResponse.ok("Primary role assigned", null));
    }

    @PostMapping("/roles/secondary")
    public ResponseEntity<ApiResponse<Void>> addSecondaryRole(@Valid @RequestBody AssignRoleRequest request) {
        assignRoleUseCase.addSecondary(request.userId(), request.roleId());
        return ResponseEntity.ok(ApiResponse.ok("Secondary role added", null));
    }
}
