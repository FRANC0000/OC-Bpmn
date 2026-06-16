package com.bpmplatform.api.dto.admin;

import jakarta.validation.constraints.NotBlank;

public record StatusRequest(
        @NotBlank String status
) {}
