package com.bpmplatform.tenant.infrastructure.persistence.repository;

import com.bpmplatform.infrastructure.multitenant.TenantSchemaMigrator;
import com.bpmplatform.tenant.domain.entity.Tenant;
import com.bpmplatform.tenant.domain.service.TenantProvisioningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TenantProvisioningServiceImpl implements TenantProvisioningService {

    private static final Logger log = LoggerFactory.getLogger(TenantProvisioningServiceImpl.class);

    private final TenantSchemaMigrator schemaMigrator;

    public TenantProvisioningServiceImpl(TenantSchemaMigrator schemaMigrator) {
        this.schemaMigrator = schemaMigrator;
    }

    @Override
    public void provision(Tenant tenant) {
        log.info("Provisioning schema {} for tenant {}", tenant.getSchemaName(), tenant.getId());
        schemaMigrator.provisionTenantSchema(tenant.getSchemaName());
    }
}
