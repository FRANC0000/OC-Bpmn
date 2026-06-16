package com.bpmplatform.infrastructure.config;

import com.bpmplatform.infrastructure.multitenant.TenantConnectionProviderImpl;
import com.bpmplatform.infrastructure.multitenant.TenantIdentifierResolverImpl;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MultiTenancyJpaConfig {

    private final TenantConnectionProviderImpl connectionProvider;
    private final TenantIdentifierResolverImpl identifierResolver;

    public MultiTenancyJpaConfig(TenantConnectionProviderImpl connectionProvider,
                                  TenantIdentifierResolverImpl identifierResolver) {
        this.connectionProvider = connectionProvider;
        this.identifierResolver = identifierResolver;
    }

    @Bean
    public HibernatePropertiesCustomizer multiTenantConnectionProviderCustomizer() {
        return (properties) -> properties.put(
                org.hibernate.cfg.AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER,
                connectionProvider);
    }

    @Bean
    public HibernatePropertiesCustomizer tenantIdentifierResolverCustomizer() {
        return (properties) -> properties.put(
                org.hibernate.cfg.AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER,
                identifierResolver);
    }
}
