# Bounded Contexts

## Context Map

```
┌─────────────────────────────────────────────────────────────────────┐
│                        BPM PLATFORM                                 │
│                                                                     │
│  ┌─────────────────┐     ┌─────────────────┐                       │
│  │   Tenant        │     │   Process       │                       │
│  │   Context       │────►│   Context       │                       │
│  │                 │     │                 │                       │
│  │ - Organization  │     │ - BPMN Design   │                       │
│  │ - Branding      │     │ - Process Def   │                       │
│  │ - Config        │     │ - Versioning    │                       │
│  │ - Plan/License  │     │ - Templates     │                       │
│  └─────────────────┘     └────────┬────────┘                       │
│                                   │                                │
│                                   ▼                                │
│  ┌─────────────────┐     ┌─────────────────┐                       │
│  │   Security      │     │   Document      │                       │
│  │   Context       │     │   Context       │                       │
│  │                 │     │                 │                       │
│  │ - Users         │     │ - Forms         │                       │
│  │ - Roles         │     │ - Metadata      │                       │
│  │ - Permissions   │     │ - Blocks/Grid   │                       │
│  │ - Auth          │     │ - Catalogs      │                       │
│  └─────────────────┘     └─────────────────┘                       │
└─────────────────────────────────────────────────────────────────────┘
```

## Context Details

### Tenant Context
- **Domain**: Organization hierarchy, branding, plans, licensing
- **Ubiquitous Language**: Tenant, Organization, Unit, Plan, Quota
- **Persistence**: Shared schema (public) for tenant registry + per-tenant schemas

### Process Context
- **Domain**: BPMN design, process execution, versioning, templates
- **Ubiquitous Language**: Process, Version, Template, Pool, UserTask, SLA
- **External**: Camunda 8 Zeebe client for execution

### Document Context
- **Domain**: Document engine, forms, blocks, metadata, catalogs, folios
- **Ubiquitous Language**: Document, Block, Grid, Metadata, Catalog, Folio
- **External**: AWS S3 for file storage

### Security Context
- **Domain**: Users, roles, permissions, authentication, audit
- **Ubiquitous Language**: User, Role, Permission, AuditLog
- **Shared**: Auth tokens for cross-context identity

## Communication

- Contexts communicate via **domain events** (in-process event bus, future event store)
- No direct repository access between contexts
- Each context owns its data and exposes it through application services
