package com.bpmplatform.api.dto.tenant;

import jakarta.validation.constraints.NotBlank;

public record ChangePlanRequest(@NotBlank String planCode) {}
