# Process Context — Análisis DDD

## Ubiquitous Language
| Término | Definición |
|---------|-----------|
| **Process** | Plantilla de proceso BPMN con versionamiento |
| **Process Version** | Versión semántica (1.0.0, 1.1.0, 2.0.0) de un proceso |
| **Process Instance** | Ejecución concreta de una versión de proceso |
| **Template** | Plantilla reutilizable de proceso (ej: Aprobación simple) |
| **Pool** | Representa un Cargo/Participante en el proceso (1:1 con Cargo) |
| **User Task** | Tarea humana dentro del proceso |
| **Business Rule Task** | Tarea automatizada con reglas de negocio |
| **SLA** | Acuerdo de nivel de servicio (tiempo esperado, máximo, escalamiento) |
| **Process Owner** | Dueño del proceso responsable de KPI, gobierno y ciclo de vida |

## Bounded Context: Process

```
┌───────────────────────────────────────────────────────────┐
│                   PROCESS CONTEXT                          │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Aggregate: ProcessDefinition                        │  │
│  │  Root: ProcessDefinition (AggregateRoot)             │  │
│  │                                                      │  │
│  │  Entities:                                           │  │
│  │  ├── ProcessVersion (Entity)                         │  │
│  │  │   ├── version: SemanticVersion (1.0.0)            │  │
│  │  │   ├── status: VersionStatus (DRAFT|ACTIVE|INACTIVE)│  │
│  │  │   ├── bpmnXml: String                             │  │
│  │  │   └── pools: List<Pool>                           │  │
│  │  │                                                    │  │
│  │  Value Objects:                                      │  │
│  │  ├── ProcessId (UUID)                                │  │
│  │  ├── SemanticVersion (major, minor, patch)           │  │
│  │  ├── Pool (name, roleId) — 1:1 con Cargo            │  │
│  │  ├── ProcessVariable (name, type, defaultValue)      │  │
│  │  ├── ProcessOwner (userId)                           │  │
│  │  └── SLADefinition (expectedTime, maxTime, escalation) │  │
│  │                                                      │  │
│  │  Domain Events:                                      │  │
│  │  ├── ProcessPublished (processId, version)           │  │
│  │  ├── ProcessVersionDeprecated (processId, version)   │  │
│  │  └── ProcessOwnerChanged (processId, oldOwner)       │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Aggregate: ProcessInstance                          │  │
│  │  Root: ProcessInstance (AggregateRoot)               │  │
│  │                                                      │  │
│  │  Value Objects:                                      │  │
│  │  ├── InstanceId (UUID)                               │  │
│  │  ├── InstanceStatus (RUNNING|COMPLETED|FAILED|CANCELLED) │
│  │  ├── Variables (Map<String, Object>)                 │  │
│  │  ├── StartedBy (userId)                              │  │
│  │  ├── StartedAt (Instant)                             │  │
│  │  └── Snapshot (frozen process definition)            │  │
│  │                                                      │  │
│  │  Domain Events:                                      │  │
│  │  ├── InstanceStarted (instanceId, processId, version)│  │
│  │  ├── InstanceCompleted (instanceId, outcome)         │  │
│  │  ├── InstanceFailed (instanceId, error)              │  │
│  │  └── SLABreached (instanceId, taskId, elapsedTime)   │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Aggregate: ProcessTemplate                          │  │
│  │  Root: ProcessTemplate (AggregateRoot)               │  │
│  │                                                      │  │
│  │  Value Objects:                                      │  │
│  │  ├── TemplateId (UUID)                               │  │
│  │  ├── TemplateCategory (string)                       │  │
│  │  ├── DefaultConfig (Map<String, Object>)             │  │
│  │  └── TemplateVersion (semantic)                      │  │
│  └──────────────────────────────────────────────────────┘  │
└───────────────────────────────────────────────────────────┘
```

## Repository Interfaces

```java
interface ProcessDefinitionRepository {
    ProcessDefinition findById(ProcessId id);
    ProcessDefinition findBySlug(String slug);
    List<ProcessDefinition> findAllByTenant(TenantId tenantId);
    ProcessDefinition save(ProcessDefinition process);
    void delete(ProcessId id);
}

interface ProcessVersionRepository {
    ProcessVersion findById(ProcessId, SemanticVersion version);
    List<ProcessVersion> findByProcessId(ProcessId id);
    ProcessVersion findActiveByProcessId(ProcessId id);
    ProcessVersion save(ProcessVersion version);
}

interface ProcessInstanceRepository {
    ProcessInstance findById(InstanceId id);
    List<ProcessInstance> findByTenant(TenantId tenantId, Pageable pageable);
    ProcessInstance save(ProcessInstance instance);
}
```

## Use Cases

| Use Case | Input | Output |
|----------|-------|--------|
| `CreateProcessDefinition` | name, description, owner | ProcessId |
| `PublishProcessVersion` | processId, bpmnXml, pools | SemanticVersion |
| `ActivateProcessVersion` | processId, version | void |
| `DeprecateProcessVersion` | processId, version | void |
| `StartProcessInstance` | processId, version, variables | InstanceId |
| `CancelProcessInstance` | instanceId, reason | void |
| `GetProcessHistory` | instanceId | InstanceHistory |
| `CreateTemplateFromProcess` | processId | TemplateId |
| `InstantiateFromTemplate` | templateId, overrides | ProcessDefinition |

## Key Rules

- Active process instances continue with their original version (never auto-migrate)
- Only one ACTIVE version per process definition
- Version semantic: 1.0.0 → 1.1.0 (minor) → 2.0.0 (major)
- Pool = 1:1 with Cargo (security context)
- SLA is defined per User Task, not per process
