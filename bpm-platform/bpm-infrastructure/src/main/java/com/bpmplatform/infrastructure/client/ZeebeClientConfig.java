package com.bpmplatform.infrastructure.client;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(name = "app.zeebe.enabled", havingValue = "true", matchIfMissing = false)
public class ZeebeClientConfig {

    private static final Logger log = LoggerFactory.getLogger(ZeebeClientConfig.class);

    private final ZeebeProperties properties;

    public ZeebeClientConfig(ZeebeProperties properties) {
        this.properties = properties;
    }

    @Bean(destroyMethod = "close")
    public ZeebeClient zeebeClient() {
        var builder = ZeebeClient.newClientBuilder()
                .gatewayAddress(properties.getGatewayAddress())
                .defaultRequestTimeout(Duration.ofMillis(properties.getDefaultRequestTimeoutMs()))
                .numJobWorkerExecutionThreads(properties.getExecutionThreads())
                .defaultJobWorkerMaxJobsActive(properties.getJobWorkerMaxJobsActive())
                .defaultJobTimeout(Duration.ofMillis(properties.getJobTimeoutMs()))
                .defaultJobPollInterval(Duration.ofMillis(properties.getJobWorkerPollIntervalMs()));

        if (properties.isSecure()) {
            builder = builder.usePlaintext();
        }

        if (properties.getClientId() != null && properties.getClientSecret() != null) {
            var credentialsProvider = new OAuthCredentialsProviderBuilder()
                    .clientId(properties.getClientId())
                    .clientSecret(properties.getClientSecret())
                    .audience(properties.getGatewayAddress())
                    .authorizationServer(properties.getOAuthUrl())
                    .build();
            builder = builder.credentialsProvider(credentialsProvider);
            log.info("Configuring Zeebe client with OAuth for cluster: {}", properties.getGatewayAddress());
        } else {
            log.info("Configuring Zeebe client with direct connection to: {}", properties.getGatewayAddress());
        }

        var client = builder.build();
        log.info("Zeebe client initialized (topology: {})", client.newTopologyRequest().send().join());
        return client;
    }
}
