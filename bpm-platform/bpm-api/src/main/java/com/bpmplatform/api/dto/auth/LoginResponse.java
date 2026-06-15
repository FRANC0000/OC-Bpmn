package com.bpmplatform.api.dto.auth;

import java.util.UUID;

public record LoginResponse(String token, UUID userId, String email, String displayName) {}
