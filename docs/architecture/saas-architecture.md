# Arquitectura SaaS

## Estrategia Multi-Tenant

### Modelo Jerárquico

```
Tenant (Cliente SaaS)
└── Organizaciones
    └── Unidades Organizacionales
```

### Aislamiento de Datos: Schema per Tenant (Actual)

```
PostgreSQL
├── public schema
│   ├── tenant_registry          ─── registro maestro de tenants
│   ├── global_catalogs           ─── catálogos compartidos (países, monedas)
│   ├── plans                     ─── planes de suscripción
│   └── flyway_schema_history     ─── migrations de schema shared
│
├── tenant_{id_1} schema
│   ├── process_definitions
│   ├── process_versions
│   ├── process_instances
│   ├── document_definitions
│   ├── document_instances
│   ├── users
│   ├── roles
│   ├── delegation
│   ├── audit_log
│   ├── catalogs                  ─── catálogos por tenant
│   └── flyway_schema_history     ─── migrations por tenant
│
└── tenant_{id_2} schema
    └── ... (misma estructura que tenant_1, datos aislados)
```

### Estrategia Futura: Database per Tenant

```
PostgreSQL Cluster
├── shared_db
│   └── (mismo contenido que public schema arriba)
│
├── tenant_{id_1}_db
│   └── public schema
│       └── (mismo contenido que tenant_{id_1} schema arriba)
│
└── tenant_{id_2}_db
    └── ...
```

Para soportar esto sin rediseñar:
- **AbstractTenantConnectionProvider** — interfaz que oculta si el tenant está en schema o database separada
- **TenantIdentifierResolver** — resuelve tenantId del contexto (header/JWT)
- **DataSourceRouting** — si es database-per-tenant, usa un `AbstractRoutingDataSource`

## Tenant Resolution

### Flujo de Resolución

```
Request
  │
  ▼
Filter: TenantIdentifierFilter
  │  Extrae tenantId de:
  │   1. Header: X-Tenant-Id
  │   2. JWT claim: tenant_id
  │   3. Subdominio: {tenant}.bpmplatform.com
  │
  ▼
CurrentTenantIdentifierResolver
  │  Implementa: String resolveCurrentTenantIdentifier()
  │  Usa: ThreadLocal<String> tenantHolder
  │
  ▼
MultiTenantConnectionProvider
  │  Implementa: Connection getConnection(String tenantId)
  │  Lógica:
  │   ├── Schema-per-tenant: connection.setSchema("tenant_" + tenantId)
  │   └── (Futuro) Database-per-tenant: lookup DataSource from registry
  │
  ▼
  Hibernate / JPA (transparente)
```

## Branding por Tenant

```
TenantConfig {
    logo: URL
    primaryColor: HexColor
    secondaryColor: HexColor
    theme: LIGHT | DARK | CORPORATE | CUSTOM
    locale: es | en
    timezone: ZoneId
    customCss: String (opcional)
}
```

El branding se expone en `/api/v1/tenants/{id}/branding` y se cachea en el frontend.

## Planes y Licenciamiento

| Recurso | Basic | Professional | Enterprise |
|---------|-------|-------------|-----------|
| Usuarios máx | 10 | 50 | Ilimitado |
| Procesos máx | 5 | 50 | Ilimitado |
| Instancias/mes | 100 | 5,000 | Ilimitado |
| Documentos | 50 | 500 | Ilimitado |
| Almacenamiento | 1GB | 10GB | Ilimitado |
| SLA | 99.5% | 99.9% | 99.99% |

### Control de Límites

- **Síncrono**: validación de cuota antes de crear recurso
- **Asíncrono**: worker que recalcula consumo periódicamente
- **Alertas**: evento `QuotaExceeded` → notificación + log
- **Upgrade**: cambio de plan sin pérdida de datos

## Migraciones Multi-Schema (Flyway)

### Estrategia

```
src/main/resources/db/
├── migration/
│   ├── shared/                    ─── migrations para schema public
│   │   ├── V1__create_tenant_registry.sql
│   │   ├── V2__create_global_catalogs.sql
│   │   └── V3__create_plans.sql
│   │
│   └── tenant/                    ─── migrations para schemas de tenants
│       ├── V1__create_process_schema.sql
│       ├── V2__create_document_schema.sql
│       ├── V3__create_security_schema.sql
│       ├── V4__create_audit_schema.sql
│       └── V5__create_catalog_schema.sql
```

Ejecución:
1. Al iniciar: `Flyway.migrate()` sobre `public` schema (shared migrations)
2. Al crear nuevo tenant: `Flyway.migrate()` sobre `tenant_{id}` schema (tenant migrations)
3. Al actualizar app: `Flyway.migrate()` sobre TODOS los schemas de tenants existentes
