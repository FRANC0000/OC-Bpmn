package com.bpmplatform.tenant.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.common.domain.DomainEventPublisher;
import com.bpmplatform.tenant.domain.repository.TenantRepository;
import com.bpmplatform.tenant.domain.valueobject.PlanCode;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class ChangeTenantPlanUseCase {

    private final TenantRepository tenantRepository;
    private final DomainEventPublisher eventPublisher;

    public ChangeTenantPlanUseCase(TenantRepository tenantRepository, DomainEventPublisher eventPublisher) {
        this.tenantRepository = tenantRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void execute(UUID tenantId, String newPlanCode) {
        var tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + tenantId));

        var newPlan = PlanCode.fromString(newPlanCode);
        tenant.changePlan(newPlan);

        tenantRepository.save(tenant);
        tenant.clearEvents().forEach(eventPublisher::publish);
    }
}
