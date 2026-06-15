package com.bpmplatform.api.dto.process;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CreateProcessRequest(
        @NotBlank String name,
        String description,
        @NotBlank @Pattern(regexp = "^[a-z0-9]+(-[a-z0-9]+)*$") String slug,
        UUID ownerUserId
) {}
