# ADR-001: Multi-module Maven Structure

**Status**: Accepted
**Date**: 2026-06-15

## Context
We need a project structure that supports modular monolith architecture with clear bounded context boundaries while remaining a single deployable unit.

## Decision
Use a multi-module Maven project with one module per bounded context plus shared modules:

- `bpm-common` — DDD seedwork (Entity, ValueObject, AggregateRoot, DomainEvent)
- `bpm-tenant-context` — Tenant management
- `bpm-process-context` — Process design & execution
- `bpm-document-context` — Document engine
- `bpm-security-context` — Users, roles, permissions
- `bpm-infrastructure` — JPA config, Zeebe client, S3 client
- `bpm-api` — Spring Boot main class, REST controllers, DTOs

## Consequences
- Clear dependency direction: API → Contexts → Common
- No circular dependencies between context modules
- Easy to extract microservices later (each context is already a JAR)
- Build time increases vs single module, but Maven incremental builds mitigate this
