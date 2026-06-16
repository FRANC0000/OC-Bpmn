package com.bpmplatform.infrastructure.messaging;

import com.bpmplatform.common.domain.DomainEvent;
import com.bpmplatform.common.domain.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(DomainEvent event) {
        eventPublisher.publishEvent(event);
    }
}
