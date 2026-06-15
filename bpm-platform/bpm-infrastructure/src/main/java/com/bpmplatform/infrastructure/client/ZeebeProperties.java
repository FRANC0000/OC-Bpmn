package com.bpmplatform.infrastructure.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.zeebe")
public class ZeebeProperties {

    private String gatewayAddress = "localhost:26500";
    private boolean secure = false;
    private String clientId;
    private String clientSecret;
    private String oAuthUrl;
    private long defaultRequestTimeoutMs = 10000;
    private long jobTimeoutMs = 300000;
    private int jobWorkerMaxJobsActive = 32;
    private long jobWorkerPollIntervalMs = 100;
    private int executionThreads = 2;
    private boolean enabled = false;

    public String getGatewayAddress() { return gatewayAddress; }
    public void setGatewayAddress(String gatewayAddress) { this.gatewayAddress = gatewayAddress; }
    public boolean isSecure() { return secure; }
    public void setSecure(boolean secure) { this.secure = secure; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
    public String getOAuthUrl() { return oAuthUrl; }
    public void setOAuthUrl(String oAuthUrl) { this.oAuthUrl = oAuthUrl; }
    public long getDefaultRequestTimeoutMs() { return defaultRequestTimeoutMs; }
    public void setDefaultRequestTimeoutMs(long defaultRequestTimeoutMs) { this.defaultRequestTimeoutMs = defaultRequestTimeoutMs; }
    public long getJobTimeoutMs() { return jobTimeoutMs; }
    public void setJobTimeoutMs(long jobTimeoutMs) { this.jobTimeoutMs = jobTimeoutMs; }
    public int getJobWorkerMaxJobsActive() { return jobWorkerMaxJobsActive; }
    public void setJobWorkerMaxJobsActive(int jobWorkerMaxJobsActive) { this.jobWorkerMaxJobsActive = jobWorkerMaxJobsActive; }
    public long getJobWorkerPollIntervalMs() { return jobWorkerPollIntervalMs; }
    public void setJobWorkerPollIntervalMs(long jobWorkerPollIntervalMs) { this.jobWorkerPollIntervalMs = jobWorkerPollIntervalMs; }
    public int getExecutionThreads() { return executionThreads; }
    public void setExecutionThreads(int executionThreads) { this.executionThreads = executionThreads; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
