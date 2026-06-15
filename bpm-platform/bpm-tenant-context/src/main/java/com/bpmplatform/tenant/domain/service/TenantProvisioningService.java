package com.bpmplatform.tenant.domain.service;

import com.bpmplatform.tenant.domain.entity.Tenant;

@FunctionalInterface
public interface TenantProvisioningService {
    void provision(Tenant tenant);
}
