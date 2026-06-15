package com.bpmplatform.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.bpmplatform")
@EntityScan(basePackages = "com.bpmplatform")
@EnableJpaRepositories(basePackages = "com.bpmplatform")
public class BpmPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(BpmPlatformApplication.class, args);
    }
}
