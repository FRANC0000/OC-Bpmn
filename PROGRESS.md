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

---

## In Progress
- (nada actualmente)

---

## Next
1. **ETAPA 2** — Implementar DDD en código: agregados, entidades, eventos de dominio, casos de uso para los 4 bounded contexts
2. **ETAPA 3** — Modelo de datos PostgreSQL, índices, performance
3. **ETAPA 4** — Integración Camunda 8 Zeebe, deploy de procesos BPMN
4. **ETAPA 5** — Document Engine, formularios, bloques, grid, metadatos, catálogos
5. **ETAPA 6** — Seguridad, usuarios, auditoría, notificaciones

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
