# Tenant Context — Análisis DDD

## Ubiquitous Language
| Término | Definición |
|---------|-----------|
| **Tenant** | Cliente SaaS con aislamiento completo de datos. Puede ser una empresa o entidad. |
| **Organization** | Unidad organizacional de alto nivel dentro de un Tenant. Ej: "Empresa A" |
| **Organizational Unit** | Subdivisión jerárquica. Ej: "Finanzas", "Compras", "RRHH" |
| **Plan** | Suscripción comercial con límites de uso (usuarios, procesos, instancias) |
| **Quota** | Límite de consumo dentro de un Plan. Ej: máximo 100 usuarios |
| **Branding** | Personalización visual del tenant (logo, colores, temas) |

## Bounded Context: Tenant

```
┌───────────────────────────────────────────────────────┐
│                 TENANT CONTEXT                         │
│                                                        │
│  ┌─────────────────────────────────────────────────┐  │
│  │  Aggregate: Tenant                              │  │
│  │  Root: Tenant (AggregateRoot)                   │  │
│  │                                                 │  │
│  │  Entities:                                      │  │
│  │  ├── Organization (Entity)                      │  │
│  │  │   └── OrganizationalUnit (Entity)             │  │
│  │  │                                                │  │
│  │  Value Objects:                                 │  │
│  │  ├── TenantId (UUID)                            │  │
│  │  ├── TenantSlug (String) — unique identifier    │  │
│  │  ├── TenantConfig (Branding, Features, Locale)  │  │
│  │  ├── Subscription (PlanId, Start, End, Status)  │  │
│  │  └── Quota (Resource, Limit, Used)              │  │
│  │                                                 │  │
│  │  Domain Events:                                 │  │
│  │  ├── TenantProvisioned (TenantId, PlanId)       │  │
│  │  ├── TenantSuspended (TenantId, Reason)         │  │
│  │  ├── PlanChanged (TenantId, OldPlan, NewPlan)   │  │
│  │  └── QuotaExceeded (TenantId, Resource)         │  │
│  └─────────────────────────────────────────────────┘  │
│                                                        │
│  ┌─────────────────────────────────────────────────┐  │
│  │  Aggregate: Plan                                │  │
│  │  Root: Plan (AggregateRoot)                     │  │
│  │                                                 │  │
│  │  Value Objects:                                 │  │
│  │  ├── PlanId (UUID)                              │  │
│  │  ├── Price (Money: Amount + Currency)           │  │
│  │  ├── PlanLimits (Map<Resource, MaxValue>)       │  │
│  │  └── BillingCycle (MONTHLY, YEARLY)             │  │
│  └─────────────────────────────────────────────────┘  │
└───────────────────────────────────────────────────────┘
```

## Repository Interfaces

```java
interface TenantRepository {
    Tenant findById(TenantId id);
    Tenant findBySlug(TenantSlug slug);
    Tenant save(Tenant tenant);
    List<Tenant> findAllActive();
}

interface PlanRepository {
    Plan findById(PlanId id);
    List<Plan> findAll();
    Plan save(Plan plan);
}
```

## Use Cases (Application Layer)

| Use Case | Input | Output | Descripción |
|----------|-------|--------|-------------|
| `ProvisionTenant` | name, slug, planId | TenantId | Crear nuevo tenant + schema DB |
| `SuspendTenant` | tenantId, reason | void | Suspender tenant por impago/incumplimiento |
| `ChangePlan` | tenantId, newPlanId | Subscription | Cambiar de plan, recalcular cuotas |
| `GetTenantConfig` | tenantId | TenantConfig | Obtener branding y configuración |
| `UpdateBranding` | tenantId, branding | void | Actualizar logo, colores, tema |
| `CheckQuota` | tenantId, resource | QuotaStatus | Verificar si un recurso tiene cupo |
| `ListOrganizations` | tenantId | List<Organization> | Obtener jerarquía organizacional |

## Architecture Decisions

- **Tenant Registry** in `public` schema (shared)
- **Tenant data** in per-tenant schema (`tenant_{id}`)
- **Schema provisioning** on first tenant creation via Flyway callback
- **Tenant resolution** via `X-Tenant-Id` header in JWT or HTTP header
