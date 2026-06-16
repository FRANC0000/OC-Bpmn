package com.bpmplatform.api;

import com.bpmplatform.document.domain.event.DocumentSubmittedEvent;
import com.bpmplatform.document.domain.event.DocumentCompletedEvent;
import com.bpmplatform.infrastructure.persistence.Notification;
import com.bpmplatform.infrastructure.persistence.NotificationRepository;
import com.bpmplatform.security.domain.event.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);
    private final NotificationRepository notificationRepository;

    public NotificationEventListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @EventListener
    public void onDocumentSubmitted(DocumentSubmittedEvent event) {
        try {
            var notification = new Notification(java.util.UUID.randomUUID(), null,
                    "document.submitted", "Documento enviado",
                    "Folio: " + event.folio(), "document_instance", event.instanceId().toString());
            notificationRepository.save(notification);
        } catch (Exception e) {
            log.warn("Failed to create notification for DocumentSubmittedEvent: {}", e.getMessage());
        }
    }

    @EventListener
    public void onDocumentCompleted(DocumentCompletedEvent event) {
        try {
            var notification = new Notification(java.util.UUID.randomUUID(), null,
                    "document.completed", "Documento completado",
                    "Folio: " + event.folio(), "document_instance", event.instanceId().toString());
            notificationRepository.save(notification);
        } catch (Exception e) {
            log.warn("Failed to create notification for DocumentCompletedEvent: {}", e.getMessage());
        }
    }

    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        try {
            var notification = new Notification(java.util.UUID.randomUUID(), event.userId(),
                    "user.registered", "Bienvenido a BPM Platform",
                    "Tu cuenta ha sido creada exitosamente", "user", event.userId().toString());
            notificationRepository.save(notification);
        } catch (Exception e) {
            log.warn("Failed to create notification for UserRegisteredEvent: {}", e.getMessage());
        }
    }
}
