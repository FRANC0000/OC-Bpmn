# Arquitectura General

## Diagrama de Componentes (Spring Boot Internals)

```
┌────────────────────────────────────────────────────────────────────────────┐
│                              SPRING BOOT API                                │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │  WEB LAYER (bpm-api)                                                  │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌───────────────┐│   │
│  │  │ Controllers  │  │   DTOs     │  │  Mappers   │  │  Exception    ││   │
│  │  │ (REST/JSON)  │  │ (Request/  │  │ (MapStruct/ │  │  Handlers    ││   │
│  │  │              │  │  Response)  │  │  Manual)    │  │  (RFC 9457)  ││   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └───────────────┘│   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │  APPLICATION LAYER (bpm-* Contexts)                                   │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                    │   │
│  │  │ Use Cases   │  │ Ports       │  │ Event      │                    │   │
│  │  │ (@UseCase)  │  │ (In/Out)   │  │ Publishers │                    │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘                    │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │  DOMAIN LAYER (bpm-* Contexts)                                        │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌───────────────┐│   │
│  │  │ Aggregate   │  │  Entities   │  │ Value      │  │ Domain Events ││   │
│  │  │ Roots       │  │             │  │ Objects    │  │ + Publisher   ││   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └───────────────┘│   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │  INFRASTRUCTURE LAYER (bpm-infrastructure)                            │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌───────────────┐│   │
│  │  │ JPA/        │  │ Multi-Tenant│  │ Zeebe      │  │ AWS S3       ││   │
│  │  │ Hibernate   │  │ Connection  │  │ Client     │  │ Client       ││   │
│  │  │ Repositories│  │ Resolver    │  │ (Camunda 8)│  │              ││   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └───────────────┘│   │
│  └──────────────────────────────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────────────────────────────┘
```

## Flujo de Ejecución de Procesos

```
1. Usuario diseña proceso BPMN en Angular Designer
       │
       ▼
2. POST /api/v1/processes/{id}/versions (BPMN XML)
       │
       ▼
3. Process Context:
   a. Valida BPMN (elementos permitidos)
   b. Crea ProcessVersion (DRAFT)
   c. Publica → version pasa a ACTIVE
   d. Deploy a Camunda 8 Zeebe: zeebeClient.newDeployCommand().addResource()
       │
       ▼
4. Usuario inicia instancia
       │
       ▼
5. POST /api/v1/processes/{id}/instances (variables)
       │
       ▼
6. Process Context:
   a. Crea ProcessInstance (snapshot de versión activa)
   b. Inicia en Zeebe: zeebeClient.newCreateInstanceCommand()
   c. Dispara evento: InstanceStarted
       │
       ▼
7. Camunda 8 ejecuta el proceso:
   a. Avanza por gateways
   b. Crea jobs para User Tasks
   c. Jobs esperan ser completados por workers
       │
       ▼
8. Job Worker (en bpm-infrastructure):
   a. Polling de jobs: zeebeClient.newWorker().jobType("user-task")
   b. Procesa job → completa en Zeebe
       │
       ▼
9. Tarea llega a Inbox del usuario (vía notificación)
       │
       ▼
10. Usuario completa tarea (formulario, adjuntos, firma)
    │
    ▼
11. POST /api/v1/instances/{id}/tasks/{taskId}/complete
    │
    ▼
12. Sistema:
    a. Guarda snapshot del documento
    b. Completa job en Zeebe
    c. Registra auditoría
    d. Notifica siguiente tarea
```

## Comunicación entre Bounded Contexts

### Principios

- **No dependencia directa** entre contextos (no se importan repositorios de otro contexto)
- **Comunicación asíncrona** vía Domain Events (in-process event bus)
- Cada contexto expone **Application Services** como interfaz de entrada
- Los contextos se acoplan solo a través de `bpm-common` (seedwork) y APIs explícitas

### Estrategia de Eventos

```
                    ┌──────────────────┐
                    │  DomainEventBus  │
                    │  (Spring Event)  │
                    └────────┬─────────┘
                             │
         ┌───────────────────┼────────────────────┐
         │                   │                    │
         ▼                   ▼                    ▼
  ┌─────────────┐   ┌──────────────┐   ┌────────────────┐
  │  Tenant     │   │   Process    │   │    Security    │
  │  Context    │   │   Context    │   │    Context     │
  └─────────────┘   └──────────────┘   └────────────────┘
         │                   │                    │
         │  Events:          │  Events:           │  Events:
         │  • TenantProvisioned  │  • InstanceStarted  │  • UserRegistered
         │  • PlanChanged    │  • InstanceCompleted│  • UserSuspended
         │  • QuotaExceeded  │  • SLABreached     │  • DelegationActivated
         └───────────────────┴────────────────────┴────────────────────┘
                                      │
                                      ▼
                              ┌────────────────┐
                              │   Document    │
                              │   Context     │
                              └────────────────┘
                              Events:
                              • DocumentSigned
                              • MetadataChanged
```

### Comunicación Futura (Event Store / Message Broker)

```
Etapa Actual:               Spring ApplicationEventPublisher (síncrono en-proceso)
Próxima Etapa:              Event Store (PostgreSQL outbox + poller)
Futuro (microservicios):    Message Broker (RabbitMQ / Kafka)
```

## Modular Monolith: Reglas de Dependencia

```
bpm-api
  ├── bpm-tenant-context
  ├── bpm-process-context
  ├── bpm-document-context
  ├── bpm-security-context
  ├── bpm-infrastructure
  └── bpm-common

bpm-{*}-context
  └── bpm-common

bpm-infrastructure
  └── bpm-common

Prohibido:
  ✗ bpm-process-context → bpm-document-context (usar eventos)
  ✗ Repositorio de un contexto en otro
  ✗ DTOs compartidos entre contextos (cada uno tiene los suyos)
```

## Stack Tecnológico Detallado

### Backend
| Dependencia | Versión | Propósito |
|------------|---------|-----------|
| Java | 21 | Platform |
| Spring Boot | 3.3.5 | Framework |
| Spring Data JPA | 3.3.x | Persistencia |
| Spring Validation | 3.3.x | Validación DTOs |
| Hibernate | 6.5.x | ORM + Multi-tenant |
| PostgreSQL JDBC | 42.7.x | Driver |
| HikariCP | 5.x | Connection pool |
| Flyway | 10.x | Migraciones multi-schema |
| Zeebe Client | 8.6.x | Camunda 8 |
| AWS SDK S3 | 2.x | File storage |
| OpenTelemetry | 1.44.x | Observabilidad |
| SpringDoc | 2.6.x | OpenAPI docs |
| Lombok | 1.18.x | Boilerplate |

### Frontend
| Dependencia | Versión | Propósito |
|------------|---------|-----------|
| Angular | 18.2.x | Framework |
| Angular Material | 18.2.x | UI components |
| BPMN.io | 17.x | BPMN modeler |
| RxJS | 7.8.x | Reactive |

## Monitoreo y Observabilidad

```
OpenTelemetry SDK
  ├── Trazas: HTTP requests, JDBC queries, Zeebe calls, S3 calls
  ├── Métricas: JVM, request rate, error rate, p95 latency
  └── Logs: structured JSON (correlationId, tenantId, userId)

Exporters:
  ├── Prometheus (métricas /actuator/prometheus)
  ├── Grafana (dashboards operacionales)
  └── OpenTelemetry Collector (traces → Jaeger/Tempo)
```
