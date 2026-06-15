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

## In Progress
- (nada actualmente)

---

## Next
1. **ETAPA 3** — Modelo de datos PostgreSQL, índices, performance
2. **ETAPA 4** — Integración Camunda 8 Zeebe, deploy de procesos BPMN
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
