package com.bpmplatform.api.dto.notification;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(UUID id, UUID userId, String type, String title,
                                    String message, String entityType, String entityId,
                                    boolean isRead, Instant createdAt) {}
