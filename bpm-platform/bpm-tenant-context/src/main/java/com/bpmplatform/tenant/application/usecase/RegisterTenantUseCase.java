package com.bpmplatform.tenant.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.common.domain.DomainEventPublisher;
import com.bpmplatform.tenant.domain.entity.Plan;
import com.bpmplatform.tenant.domain.entity.Tenant;
import com.bpmplatform.tenant.domain.repository.PlanRepository;
import com.bpmplatform.tenant.domain.repository.TenantRepository;
import com.bpmplatform.tenant.domain.service.TenantProvisioningService;
import com.bpmplatform.tenant.domain.valueobject.PlanCode;
import com.bpmplatform.tenant.domain.valueobject.SchemaName;
import com.bpmplatform.tenant.domain.valueobject.Slug;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class RegisterTenantUseCase {

    private final TenantRepository tenantRepository;
    private final PlanRepository planRepository;
    private final TenantProvisioningService provisioningService;
    private final DomainEventPublisher eventPublisher;

    public RegisterTenantUseCase(TenantRepository tenantRepository, PlanRepository planRepository,
                                 TenantProvisioningService provisioningService, DomainEventPublisher eventPublisher) {
        this.tenantRepository = tenantRepository;
        this.planRepository = planRepository;
        this.provisioningService = provisioningService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Output execute(Input input) {
        if (tenantRepository.existsBySlug(input.slug())) {
            throw new IllegalArgumentException("Slug already in use: " + input.slug());
        }

        var tenantId = UUID.randomUUID();
        var slug = new Slug(input.slug());
        var schemaName = new SchemaName("tenant_" + tenantId.toString().replace("-", ""));
        var planCode = PlanCode.fromString(input.planCode());

        Plan plan = planRepository.findByCode(planCode.code())
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan: " + input.planCode()));

        var tenant = new Tenant(tenantId, input.name(), slug, schemaName, planCode);

        tenantRepository.save(tenant);
        provisioningService.provision(tenant);

        tenant.clearEvents().forEach(eventPublisher::publish);

        return new Output(tenant.getId(), tenant.getSlug(), tenant.getSchemaName(), tenant.getPlan());
    }

    public record Input(String name, String slug, String planCode) {}
    public record Output(UUID tenantId, String slug, String schemaName, String plan) {}
}
