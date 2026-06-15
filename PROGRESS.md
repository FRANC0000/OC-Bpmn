# PROGRESS — BPM Platform

> Auditoría de avances del proyecto. Actualizado al completar cada etapa/hito.

---

## Done

### Hito 0 — Scaffolding inicial
- `Base.md` prompt maestro creado y optimizado (970 → 616 líneas)
- `Base.md` dividido en 14 archivos bajo `prompts/`
- Repositorio Git inicializado (`git init`, branch `main`)
- `.gitignore` con exclusiones para Java, Maven, Angular, Node, IDE, OS, Docker
- Remoto conectado: `https://github.com/FRANC0000/OC-Bpmn`

### Hito 1 — Arquitectura General, SaaS, C4 y DDD

**Backend**
- Proyecto Spring Boot multi-módulo Maven con 7 módulos (Java 21):
  - `bpm-api` — REST API, DTOs, controllers, exception handlers
  - `bpm-common` — Base Domain (AggregateRoot, Entity, ValueObject), UseCase, DomainEvent
  - `bpm-tenant-context` — Bounded Context de tenants/planes
  - `bpm-process-context` — Bounded Context de procesos BPMN
  - `bpm-document-context` — Bounded Context de documentos dinámicos
  - `bpm-security-context` — Bounded Context de seguridad/usuarios
  - `bpm-infrastructure` — Multi-tenancy, Flyway, messaging, persistence
- `application.yml` con Hibernate SCHEMA multi-tenancy + Flyway
- Estructura hexagonal en cada módulo: `domain/`, `application/`, `infrastructure/`

**Multi-tenancy**
- `TenantContext` (ThreadLocal) — resuelve tenant activo por request
- `TenantIdentifierFilter` — Filter HTTP que extrae `X-Tenant-Id` o JWT
- `TenantIdentifierResolverImpl` — implementación de `CurrentTenantIdentifierResolver`
- `TenantConnectionProviderImpl` — `AbstractDataSourceBasedMultiTenantConnectionProviderImpl`
- `TenantSchemaMigrator` + `TenantMigrationRunner` — Flyway multi-schema

**Migraciones SQL**
- Shared (schema `public`): V1 tenant_registry, V2 plans
- Tenant: V1 process, V2 document, V3 security, V4 audit, V5 catalog

**Frontend**
- Angular 18 standalone con lazy loading en 6 rutas:
  - `/auth` — Login/registro
  - `/tasks` — Bandeja de tareas
  - `/processes` — Lista y diseño de procesos (BPMN)
  - `/designer` — Editor de formularios/bloques
  - `/documents` — Gestión documental
  - `/admin` — Administración de tenants, usuarios, planes

**Infraestructura**
- `docker-compose.yml` con PostgreSQL 16 + pgAdmin
- `scripts/setup-git-bash.sh` — configura npm PATH en Git Bash
- `scripts/fix-ng-path.ps1` — alternativa PowerShell para npm PATH

**Documentación**
- `README.md` con instrucciones de setup completo
- C4 Nivel 1 (Contexto) y Nivel 2 (Contenedores)
- Diagrama de Bounded Contexts (4 contextos)
- Arquitectura SaaS detallada (tenant resolution, branding, planes, Flyway multi-schema)
- Arquitectura General (componentes, flujo de ejecución, eventos entre BCs)
- DDD completo para los 4 Bounded Contexts (ubiquitous language, agregados, eventos, repositorios, casos de uso)
- ADR-001: Multi-módulo Maven
- ADR-002: Multi-tenancy schema-per-tenant
- ADR-003: Camunda 8 SaaS

**Commits**
| Hash | Mensaje |
|------|---------|
| `a80df77` | chore: initial scaffolding of BPM Platform |
| `58f3646` | feat: ETAPA 1 - Arquitectura General, SaaS, C4 y DDD |
| `f680cff` | chore: add .angular/cache to gitignore and clean tracked cache |
| `85a9c8f` | docs: add PROGRESS.md with audit trail of project milestones |
| `4cfa923` | feat: ETAPA 2 - Implementacion DDD en codigo (86 archivos nuevos) |

### Hito 2 — Implementación DDD en código

**Base común (`bpm-common`)**
- `AggregateRoot`, `Entity`, `ValueObject` actualizados con JPA (`@MappedSuperclass`, `@Id`, `@Access`, `@Transient`)
- `spring-boot-starter-data-jpa` agregado como dependencia

**bpm-infrastructure**
- `SpringDomainEventPublisher` — adaptador de `DomainEventPublisher` sobre `ApplicationEventPublisher`
- `DomainEventConfig` — configuración del event publisher
- `BaseJpaEntity` — `@MappedSuperclass` con `createdAt`/`updatedAt` automáticos

**bpm-tenant-context** (20 archivos)
- Value Objects: `Slug`, `SchemaName`, `PlanCode`, `TenantStatus`
- Entidades: `Tenant` (AR), `Plan` (Entity)
- Eventos: `TenantRegisteredEvent`, `TenantPlanChangedEvent`
- Repositorios: `TenantRepository`, `PlanRepository`
- Servicio dominio: `TenantProvisioningService`
- Casos de uso: `RegisterTenantUseCase`, `ChangeTenantPlanUseCase`
- JPA: converters, Spring Data repos, impls, `TenantProvisioningServiceImpl`

**bpm-security-context** (19 archivos)
- Value Objects: `Email`, `PasswordHash`, `DisplayName`, `RoleType`, `UserStatus`
- Entidades: `User` (AR) con primary+secondary roles, `Role` (Entity)
- Eventos: `UserRegisteredEvent`
- Repositorios: `UserRepository`, `RoleRepository`
- Casos de uso: `RegisterUserUseCase`, `AssignRoleUseCase`
- JPA: converters, Spring Data repos, impls

**bpm-process-context** (15 archivos)
- Value Objects: `ProcessStatus`, `VersionStatus`
- Entidades: `ProcessDefinition` (AR) con versions cascade, `ProcessVersion`, `ProcessTemplate`
- Eventos: `ProcessCreatedEvent`, `ProcessVersionPublishedEvent`
- Repositorios: `ProcessDefinitionRepository`, `ProcessTemplateRepository`
- Casos de uso: `CreateProcessDefinitionUseCase`, `PublishProcessVersionUseCase`
- JPA: converters, Spring Data repos, impls

**bpm-document-context** (17 archivos)
- Value Objects: `DocumentStatus`, `InstanceStatus`
- Entidades: `DocumentDefinition` (AR) con versions cascade, `DocumentVersion`, `DocumentInstance` (AR), `FolioSequence`
- Eventos: `DocumentDefinitionCreatedEvent`, `DocumentSubmittedEvent`, `DocumentCompletedEvent`
- Repositorios: `DocumentDefinitionRepository`, `DocumentInstanceRepository`, `FolioSequenceRepository`
- Casos de uso: `CreateDocumentDefinitionUseCase`, `SubmitDocumentUseCase`
- JPA: Spring Data repos, impls

**bpm-api** (15 archivos: 4 controllers + 11 DTOs)
- `TenantController` — POST `/api/v1/tenants`, POST `/{id}/change-plan`
- `SecurityController` — POST `/api/v1/security/users`, `/roles/primary`, `/roles/secondary`
- `ProcessController` — POST `/api/v1/processes`, `/publish-version`
- `DocumentController` — POST `/api/v1/documents/definitions`, `/submit`
- DTOs genéricos: `ApiResponse<T>`
- DTOs por contexto: request/response records con `jakarta.validation`

**Total: ~86 archivos Java nuevos** (6 módulos actualizados)

**POMs actualizados**
- `bpm-common`: agregado `spring-boot-starter-data-jpa`
- `bpm-{tenant,security,process,document}-context`: agregado `spring-boot-starter-data-jpa`

---

### Hito 3 — Modelo de datos PostgreSQL, índices y performance

**Nuevas migraciones Flyway**

| Migración | Descripción |
|-----------|-------------|
| `shared/V3__add_shared_performance_indexes.sql` | 5 índices, check constraints, funciones de resolución de tenant |
| `tenant/V6__add_tenant_performance_indexes.sql` | ~50 índices incluyendo GIN en JSONB, check constraints |

**Índices agregados por contexto:**

| Contexto | Tabla | Índices nuevos |
|----------|-------|----------------|
| **Shared** | `tenant_registry` | plan, schema_name, composite (status, plan) |
| **Shared** | `plans` | is_active, partial (code WHERE active) |
| **Process** | `process_definitions` | owner, status, (slug, status), updated_at |
| **Process** | `process_versions` | status, created_by, created_at, (process_id, status, version), FTS bpmn_xml |
| **Process** | `process_templates` | category, published, (category, published), created_at |
| **Document** | `document_definitions` | status, updated_at |
| **Document** | `document_versions` | status, created_by, created_at, (doc_id, status, version), **GIN** blocks/metadata |
| **Document** | `document_instances` | (doc_id, version), status, created_by, process_instance, created_at, (status, created_at), **GIN** values, completed_at |
| **Document** | `folio_sequences` | UNIQUE (format, year) |
| **Security** | `roles` | type, system |
| **Security** | `users` | status, tenant, primary_role, (tenant, status), (tenant, email) |
| **Audit** | `audit_log` | tenant, action, (action, created_at), (tenant, entity), **GIN** details |
| **Audit** | `delegations` | from, to, active, (from, active), (to, active), (start, end) |
| **Catalog** | `catalogs` | active, level, (level, active), partial (code WHERE active) |
| **Catalog** | `catalog_items` | active, (catalog_id, active, sort_order) |

**Check constraints agregadas:** status values en todas las tablas, tipos de planes, monedas, scopes, niveles de catálogo

**Funciones PostgreSQL:**
- `resolve_tenant_schema(p_identifier)` — lookup de schema por slug o id (PARALLEL SAFE)
- `get_active_tenant_schemas()` — lista schemas activos para migraciones batch

**Configuración de performance (`application.yml`):**
- HikariCP: pool 5-20, timeouts, leak detection, connection validation
- JPA batch: batch_size=50, order_inserts/updates, batch_versioned_data
- Query: plan_cache=4096, in_clause_parameter_padding, fetch_size=200
- Connection: provider_disables_autocommit, autocommit=false

**PostgreSQL tuning (`docker-compose.yml`):**
- shared_buffers=256MB, effective_cache_size=768MB, work_mem=16MB
- maintenance_work_mem=64MB, random_page_cost=1.1 (SSD)
- effective_io_concurrency=200, wal_buffers=16MB
- Autovacuum optimizado (scale_factor más agresivo)
- log_min_duration_statement=200ms (slow query log)
- Límites de memoria (1GB container limit)

---

## In Progress
- (nada actualmente)

---

## Next
1. **ETAPA 4** — Integración Camunda 8 Zeebe, deploy de procesos BPMN
3. **ETAPA 5** — Document Engine, formularios, bloques, grid, metadatos, catálogos
4. **ETAPA 6** — Seguridad, usuarios, auditoría, notificaciones

---

## Decisiones clave
| Fecha | Decisión | Alternativa descartada |
|-------|----------|------------------------|
| 2026-06-15 | **Camunda 8 SaaS** (Zeebe client) | Camunda 7 self-hosted |
| 2026-06-15 | **Maven multi-módulo** (7 módulos) | Maven single-module, Gradle |
| 2026-06-15 | **Angular 18 standalone** | Angular con NgModules |
| 2026-06-15 | **Schema-per-tenant** via `AbstractDataSourceBasedMultiTenantConnectionProviderImpl` | Database-per-tenant, discriminator column |
| 2026-06-15 | **Flyway multi-schema** (shared + tenant migrations) | Liquibase, manual scripts |
| 2026-06-15 | **Modular Monolith** con eventos Spring | Microservicios desde el día 1 |

---

## Estructura del proyecto
```
D:\OpenCode\
├── angular-bpm/          # Frontend Angular 18 standalone
├── bpm-platform/         # Backend Spring Boot multi-módulo
│   ├── bpm-api/
│   ├── bpm-common/
│   ├── bpm-tenant-context/
│   ├── bpm-process-context/
│   ├── bpm-document-context/
│   ├── bpm-security-context/
│   └── bpm-infrastructure/
├── docs/
│   ├── architecture/     # C4, SaaS, Bounded Contexts, General
│   ├── ddd/              # Análisis DDD por contexto
│   └── decisions/        # ADR-001, ADR-002, ADR-003
├── prompts/              # 14 prompts por módulo
├── scripts/              # setup-git-bash.sh, fix-ng-path.ps1
├── docker-compose.yml    # PostgreSQL 16 + pgAdmin
├── PROGRESS.md           # ← este archivo
└── README.md
```

---

## Entorno
| Componente | Estado |
|------------|--------|
| Node.js | No instalado globalmente. Funciona vía `npm start` desde `angular-bpm/` |
| Angular CLI | No global. Accesible via `node_modules/.bin/ng` |
| Java 21 | Pendiente de instalación/verificación |
| Maven | Pendiente de instalación/verificación |
| PostgreSQL | Pendiente de inicio (`docker compose up -d`) |
| Git Bash | `.bashrc` actualizado con npm PATH |
