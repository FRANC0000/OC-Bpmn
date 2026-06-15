# Security Context — Análisis DDD

## Ubiquitous Language
| Término | Definición |
|---------|-----------|
| **User** | Persona que accede al sistema con credenciales |
| **Role / Cargo** | Conjunto de permisos asignables a usuarios. Son equivalentes en esta versión |
| **Permission** | Acción granular: `document.create`, `process.publish`, `users.manage` |
| **Assignment** | Relación usuario-tarea (por usuario específico o por cargo) |
| **Delegation** | Transferencia temporal de tareas de Usuario A a Usuario B |
| **Subrogation** | Reemplazo oficial de Usuario A por Usuario B (impacta todo) |
| **Audit Log** | Registro inmutable de acciones: quién, cuándo, qué, entidad |

## Bounded Context: Security

```
┌───────────────────────────────────────────────────────────────┐
│                   SECURITY CONTEXT                             │
│                                                               │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Aggregate: User                                        │  │
│  │  Root: User (AggregateRoot)                             │  │
│  │                                                         │  │
│  │  Entities:                                              │  │
│  │  ├── RoleAssignment (Entity) — user ↔ role relationship │  │
│  │  │                                                    │  │
│  │  Value Objects:                                        │  │
│  │  ├── UserId (UUID)                                     │  │
│  │  ├── Email (value object with validation)              │  │
│  │  ├── UserStatus (ACTIVE|SUSPENDED|BLOCKED|DELETED)    │  │
│  │  ├── PrimaryRole (RoleId)                              │  │
│  │  ├── SecondaryRoles (List<RoleId>)                     │  │
│  │  └── Credentials (hashedPassword, lastChanged)         │  │
│  │                                                         │  │
│  │  Domain Events:                                         │  │
│  │  ├── UserRegistered (userId, email, tenantId)           │  │
│  │  ├── UserSuspended (userId, reason)                    │  │
│  │  ├── UserRoleChanged (userId, oldRole, newRole)        │  │
│  │  └── UserDeleted (userId)                               │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                               │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Aggregate: Role                                        │  │
│  │  Root: Role (AggregateRoot)                             │  │
│  │                                                         │  │
│  │  Value Objects:                                         │  │
│  │  ├── RoleId (UUID)                                      │  │
│  │  ├── RoleName (String) — unique per tenant              │  │
│  │  ├── Permissions (Set<Permission>)                      │  │
│  │  └── RoleType (PRIMARY|SECONDARY)                       │  │
│  │                                                         │  │
│  │  Domain Events:                                         │  │
│  │  ├── RoleCreated (roleId, name, permissions)            │  │
│  │  ├── PermissionsUpdated (roleId, added, removed)       │  │
│  │  └── RoleDeleted (roleId)                               │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                               │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Aggregate: AuditLog                                    │  │
│  │  Root: AuditLog (Entity) — no AggregateRoot needed      │  │
│  │                                                         │  │
│  │  Value Objects:                                         │  │
│  │  ├── AuditEntry (userId, action, entity, timestamp)     │  │
│  │  ├── EntityReference (entityType, entityId)             │  │
│  │  ├── ActionType (CREATE|UPDATE|DELETE|PUBLISH|SIGN)    │  │
│  │  └── AuditSnapshot (previousState, newState)            │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                               │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Aggregate: Delegation                                  │  │
│  │  Root: Delegation (AggregateRoot)                       │  │
│  │                                                         │  │
│  │  Value Objects:                                         │  │
│  │  ├── DelegationId (UUID)                                │  │
│  │  ├── FromUser (UserId)                                  │  │
│  │  ├── ToUser (UserId)                                    │  │
│  │  ├── Period (startDate, endDate)                        │  │
│  │  ├── Scope (ALL_TASKS | SPECIFIC_PROCESSES)             │  │
│  │  └── Subrogation (boolean) — true = subrogation         │  │
│  │                                                         │  │
│  │  Domain Events:                                         │  │
│  │  ├── TaskDelegated (delegationId, fromUser, toUser)     │  │
│  │  ├── SubrogationActivated (subrogationId, replacement)  │  │
│  │  └── DelegationRevoked (delegationId)                   │  │
│  └──────────────────────────────────────────────────────────┘  │
└───────────────────────────────────────────────────────────────┘
```

## Repository Interfaces

```java
interface UserRepository {
    User findById(UserId id);
    User findByEmail(Email email);
    List<User> findByTenant(TenantId tenantId, Pageable pageable);
    User save(User user);
    void delete(UserId id);
}

interface RoleRepository {
    Role findById(RoleId id);
    Role findByName(String name, TenantId tenantId);
    List<Role> findByTenant(TenantId tenantId);
    Role save(Role role);
    void delete(RoleId id);
}

interface AuditLogRepository {
    void append(AuditEntry entry);
    List<AuditEntry> findByEntity(EntityReference ref, Pageable pageable);
    List<AuditEntry> findByUser(UserId userId, Pageable pageable);
}

interface DelegationRepository {
    Delegation findById(DelegationId id);
    List<Delegation> findActiveByFromUser(UserId userId);
    List<Delegation> findActiveByToUser(UserId userId);
    Delegation save(Delegation delegation);
    void revoke(DelegationId id);
}
```

## Use Cases

| Use Case | Input | Output |
|----------|-------|--------|
| `RegisterUser` | email, password, role, tenantId | UserId |
| `UpdateUserRoles` | userId, primaryRole, secondaryRoles | void |
| `SuspendUser` | userId, reason | void |
| `Authenticate` | email, password, tenantId | AuthToken |
| `CreateRole` | name, permissions, tenantId | RoleId |
| `UpdateRolePermissions` | roleId, permissions | void |
| `CheckPermission` | userId, permission | boolean |
| `DelegateTask` | fromUser, toUser, period, scope | DelegationId |
| `ActivateSubrogation` | fromUser, toUser, period | DelegationId |
| `AppendAuditLog` | userId, action, entity, snapshot | void |
| `QueryAuditLog` | entityRef, pageable | Page<AuditEntry> |

## Permission Naming Convention

```
{resource}.{action}

Examples:
document.create    document.edit    document.publish
process.create     process.publish  process.delete
metadata.manage
users.manage
audit.view
dashboard.view
```

## Key Rules

- Role = Cargo (equivalentes en esta versión)
- User can have 1 primary role + multiple secondary roles
- Delegation is temporal (always has end date)
- Subrogation replaces user at system level (inbox, assignment, audit, notifications)
- AuditLog is append-only (immutable, no deletes)
- Permissions are granted per role, not per user
