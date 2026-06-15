# ADR-002: Multi-tenant Strategy

**Status**: Accepted
**Date**: 2026-06-15

## Context
The platform is SaaS with multiple tenants. We need an isolation strategy that balances security, scalability, and complexity.

## Decision
Use **Schema-per-Tenant** as the primary strategy:

- A shared `public` schema holds tenant registry, global catalogs, and user authentication
- Each tenant gets an isolated PostgreSQL schema (`tenant_{id}`)
- Hibernate MultiTenancyConnectionProvider routes requests by tenant
- Tenant is identified via header (`X-Tenant-Id`) or JWT claim

Future evolution to **Database-per-Tenant** is supported:
- Abstract `TenantConnectionProvider` interface
- Current implementation uses schema within same database
- Future implementation can switch to separate database connection pool

## Consequences
- Strong data isolation — schema is a hard boundary
- Shared connection pool, lower resource usage vs database-per-tenant
- Schema migration must be tenant-aware (Flyway with tenant init)
- Row-level security not needed (schema provides isolation)
