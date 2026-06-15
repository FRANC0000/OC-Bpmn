package com.bpmplatform.common.domain;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
