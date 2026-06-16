package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.notification.NotificationResponse;
import com.bpmplatform.infrastructure.multitenant.TenantContext;
import com.bpmplatform.infrastructure.persistence.NotificationRepository;
import com.bpmplatform.security.infrastructure.auth.BpmUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
            @AuthenticationPrincipal BpmUserDetails user) {
        return withTenantContext(user, () -> {
            var notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getUserId())
                    .stream()
                    .map(n -> new NotificationResponse(n.getId(), n.getUserId(), n.getType(), n.getTitle(),
                            n.getMessage(), n.getEntityType(), n.getEntityId(), n.isRead(), n.getCreatedAt()))
                    .toList();
            return ResponseEntity.ok(ApiResponse.ok("Notifications retrieved", notifications));
        });
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@AuthenticationPrincipal BpmUserDetails user) {
        return withTenantContext(user, () -> {
            var count = notificationRepository.countByUserIdAndIsRead(user.getUserId(), false);
            return ResponseEntity.ok(ApiResponse.ok("Unread count", count));
        });
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable UUID id,
                                                         @AuthenticationPrincipal BpmUserDetails user) {
        return withTenantContext(user, () -> {
            var notification = notificationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + id));

            if (!notification.getUserId().equals(user.getUserId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("Access denied"));
            }

            notification.markAsRead();
            notificationRepository.save(notification);
            return ResponseEntity.ok(ApiResponse.ok("Notification marked as read", null));
        });
    }

    private <T> ResponseEntity<T> withTenantContext(BpmUserDetails user, Supplier<ResponseEntity<T>> block) {
        var schema = "tenant_" + user.getTenantId().toString().replace("-", "");
        TenantContext.setTenantId(schema);
        try {
            return block.get();
        } finally {
            TenantContext.clear();
        }
    }
}
