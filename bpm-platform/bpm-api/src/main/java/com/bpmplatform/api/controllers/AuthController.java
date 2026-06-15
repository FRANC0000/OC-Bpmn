package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.auth.LoginRequest;
import com.bpmplatform.api.dto.auth.LoginResponse;
import com.bpmplatform.security.application.usecase.LoginUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        var input = new LoginUseCase.Input(request.email(), request.password());
        var output = loginUseCase.execute(input);
        var response = new LoginResponse(output.token(), output.userId(), output.email(), output.displayName());
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }
}
