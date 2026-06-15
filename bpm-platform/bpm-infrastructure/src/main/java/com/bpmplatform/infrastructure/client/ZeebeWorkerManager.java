package com.bpmplatform.infrastructure.client;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnBean(ZeebeClient.class)
public class ZeebeWorkerManager implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(ZeebeWorkerManager.class);

    private final ZeebeClient client;
    private final ConcurrentHashMap<String, AutoCloseable> workers = new ConcurrentHashMap<>();

    public ZeebeWorkerManager(ZeebeClient client) {
        this.client = client;
    }

    public void registerWorker(String jobType, JobHandler handler, String workerName) {
        var registration = client.newWorker()
                .jobType(jobType)
                .handler(handler)
                .name(workerName)
                .maxJobsActive(32)
                .timeout(300_000)
                .pollInterval(java.time.Duration.ofMillis(100))
                .open();

        workers.put(jobType, registration);
        log.info("Registered Zeebe worker for job type '{}' (name: {})", jobType, workerName);
    }

    public void registerWorker(String jobType, JobHandler handler) {
        registerWorker(jobType, handler, "bpm-platform-" + jobType);
    }

    public void unregisterWorker(String jobType) {
        var registration = workers.remove(jobType);
        if (registration != null) {
            try { registration.close(); } catch (Exception e) {
                log.warn("Error closing worker for '{}'", jobType, e);
            }
            log.info("Unregistered Zeebe worker for job type '{}'", jobType);
        }
    }

    @Override
    public void destroy() {
        workers.keySet().forEach(this::unregisterWorker);
        log.info("All Zeebe workers shut down");
    }
}
