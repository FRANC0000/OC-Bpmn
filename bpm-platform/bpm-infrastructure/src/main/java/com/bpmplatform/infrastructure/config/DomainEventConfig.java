package com.bpmplatform.infrastructure.config;

import com.bpmplatform.common.domain.DomainEventPublisher;
import com.bpmplatform.infrastructure.messaging.SpringDomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventConfig {

    @Bean
    public DomainEventPublisher domainEventPublisher(ApplicationEventPublisher eventPublisher) {
        return new SpringDomainEventPublisher(eventPublisher);
    }
}
