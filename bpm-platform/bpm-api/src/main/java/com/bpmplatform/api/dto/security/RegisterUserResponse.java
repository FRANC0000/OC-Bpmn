package com.bpmplatform.api.dto.security;

import java.util.UUID;

public record RegisterUserResponse(UUID userId, String email, String displayName) {}
