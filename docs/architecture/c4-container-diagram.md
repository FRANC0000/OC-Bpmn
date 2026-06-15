# C4 Container Diagram — Nivel 2

```
┌─────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                  BPM PLATFORM (SaaS)                                                 │
│                                                                                                       │
│  ┌──────────────────────────────────────────────┐                                                    │
│  │   Angular SPA (Container)                    │                                                    │
│  │                                              │                                                    │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────────┐ │  [HTTP/JSON]                                       │
│  │  │ Auth     │ │ Designer │ │ Feature      │ │◄────────────────────┐                               │
│  │  │ Module   │ │ Module   │ │ Modules      │ │                      │                               │
│  │  │          │ │          │ │ (lazy)       │ │                      │                               │
│  │  ├──────────┤ ├──────────┤ ├──────────────┤ │                      │                               │
│  │  │ Core     │ │ Shared   │ │ Layout       │ │                      │                               │
│  │  │ (HTTP,   │ │ (UI kit) │ │ (Shell)      │ │                      │                               │
│  │  │ Auth)    │ │          │ │              │ │                      │                               │
│  │  └──────────┘ └──────────┘ └──────────────┘ │                      │                               │
│  └──────────────────────────────────────────────┘                      │                               │
│                                                                        │                               │
│  ┌──────────────────────────────────────────────────────────────────────────────────────┐            │
│  │   Spring Boot API (Container)                                                          │            │
│  │                                                                                         │            │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐   │            │
│  │  │  API Layer (bpm-api)                                                            │   │            │
│  │  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────────────┐   │   │            │
│  │  │  │ REST         │ │ OpenAPI      │ │ Global       │ │ DTOs / Mappers       │   │   │            │
│  │  │  │ Controllers  │ │ Docs         │ │ Exception    │ │                      │   │   │            │
│  │  │  │              │ │ (Swagger)    │ │ Handler      │ │                      │   │   │            │
│  │  │  └──────────────┘ └──────────────┘ └──────────────┘ └──────────────────────┘   │   │            │
│  │  └─────────────────────────────────────────────────────────────────────────────────┘   │            │
│  │                                                                                         │            │
│  │  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐  │            │
│  │  │  Tenant Context  │  │  Process Context │  │ Document Context │  │ Security Context │  │            │
│  │  │  (bpm-tenant)    │  │  (bpm-process)   │  │  (bpm-document)  │  │  (bpm-security)  │  │            │
│  │  │                  │  │                  │  │                  │  │                  │  │            │
│  │  │ ┌────┐ ┌──────┐ │  │ ┌────┐ ┌──────┐  │  │ ┌────┐ ┌──────┐  │  │ ┌────┐ ┌──────┐  │  │            │
│  │  │ │    │ │      │ │  │ │    │ │      │  │  │ │    │ │      │  │  │ │    │ │      │  │  │            │
│  │  │ │Dom.│ │ App. │ │  │ │Dom.│ │ App. │  │  │ │Dom.│ │ App. │  │  │ │Dom.│ │ App. │  │  │            │
│  │  │ │    │ │      │ │  │ │    │ │      │  │  │ │    │ │      │  │  │ │    │ │      │  │  │            │
│  │  │ └────┘ └──────┘ │  │ └────┘ └──────┘  │  │ └────┘ └──────┘  │  │ └────┘ └──────┘  │  │            │
│  │  └──────────────────┘  └──────────────────┘  └──────────────────┘  └──────────────────┘  │            │
│  │                                                                                         │            │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐   │            │
│  │  │  Infrastructure (bpm-infrastructure)                                            │   │            │
│  │  │  ┌────────────────┐ ┌────────────────┐ ┌──────────────┐ ┌──────────────────┐   │   │            │
│  │  │  │ JPA / Hibernate│ │ Multi-Tenant   │ │ Zeebe Client │ │ AWS S3 Client   │   │   │            │
│  │  │  │ (PostgreSQL)   │ │ Connection     │ │ (Camunda 8)  │ │ (File Storage)  │   │   │            │
│  │  │  │                │ │ Resolver       │ │              │ │                 │   │   │            │
│  │  │  └────────────────┘ └────────────────┘ └──────────────┘ └──────────────────┘   │   │            │
│  │  └─────────────────────────────────────────────────────────────────────────────────┘   │            │
│  └──────────────────────────────────────────────────────────────────────────────────────┘            │
│                                                                                                       │
│  ┌────────────────────────────────┐      ┌────────────────────────────────┐                          │
│  │  PostgreSQL (Container)        │      │  AWS S3 (Container)            │                          │
│  │                                 │      │                                │                          │
│  │  ┌──────────────────────────┐   │      │  ┌──────────────────────────┐  │                          │
│  │  │ public schema            │   │      │  │ tenant-files/            │  │                          │
│  │  │ - tenant_registry        │   │      │  │  ├── {tenant-id}/        │  │                          │
│  │  │ - global_catalogs        │   │      │  │  │   ├── processes/      │  │                          │
│  │  │ - users (auth)           │   │      │  │  │   ├── documents/      │  │                          │
│  │  ├──────────────────────────┤   │      │  │  │   └── attachments/    │  │                          │
│  │  │ tenant_1 schema          │   │      │  └──────────────────────────┘  │                          │
│  │  │ - processes              │   │      └────────────────────────────────┘                          │
│  │  │ - documents              │   │                                                                  │
│  │  │ - metadata               │   │      ┌────────────────────────────────┐                          │
│  │  ├──────────────────────────┤   │      │  Camunda 8 SaaS (Container)    │                          │
│  │  │ tenant_2 schema          │   │      │                                │                          │
│  │  │ - processes              │   │      │  ┌────────────┐ ┌───────────┐  │                          │
│  │  │ - documents              │   │      │  │ Zeebe      │ │ Operate   │  │                          │
│  │  │ - metadata               │   │      │  │ (Engine)   │ │ (Monitor) │  │                          │
│  │  └──────────────────────────┘   │      │  └────────────┘ └───────────┘  │                          │
│  └────────────────────────────────┘      │  ┌────────────┐                  │                          │
│                                          │  │ Tasklist   │                  │                          │
│                                          │  │ (UI)       │                  │                          │
│                                          │  └────────────┘                  │                          │
│                                          └────────────────────────────────┘                          │
└─────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

## Contenedores

| Contenedor | Tecnología | Descripción | Responsabilidades |
|------------|-----------|-------------|-------------------|
| **Angular SPA** | Angular 18, Material, BPMN.io | Single Page Application | UI/UX, Designer, Form Builder, Dashboard |
| **Spring Boot API** | Java 21, Spring Boot 3.3 | Backend monolito modular | REST API, Lógica de dominio, Orquestación |
| **PostgreSQL** | PostgreSQL 16 | Base de datos relacional | Persistencia multi-tenant, Schemas por tenant |
| **AWS S3** | AWS S3 | Almacenamiento de objetos | Archivos, adjuntos, documentos |
| **Camunda 8** | Zeebe, Operate, Tasklist | Workflow Engine SaaS | Ejecución de procesos BPMN, Job workers |

## Relaciones entre Contenedores

| De | A | Protocolo | Propósito |
|----|---|-----------|-----------|
| Angular SPA | Spring Boot API | HTTP/JSON (REST) | Todas las operaciones CRUD y queries |
| Spring Boot API | PostgreSQL | JDBC/HikariCP | Persistencia de datos (multi-schema) |
| Spring Boot API | AWS S3 | AWS SDK (HTTPS) | Subida/descarga de archivos |
| Spring Boot API | Camunda 8 | gRPC (Zeebe client) | Deploy procesos, crear instancias, completar jobs |
| Angular SPA | Camunda 8 Tasklist | HTTP (Browser) | Redirección opcional a UI de tareas |

## Tecnologías por Contenedor

### Spring Boot API
- **Web**: Spring Web MVC, Tomcat embedded
- **Persistencia**: Spring Data JPA, Hibernate 6, HikariCP
- **Multi-tenant**: Hibernate MultiTenancyConnectionProvider + CurrentTenantIdentifierResolver
- **Migraciones**: Flyway (multi-schema)
- **Camunda**: Zeebe Client Java 8.x
- **S3**: AWS SDK S3 v2
- **Observabilidad**: OpenTelemetry auto-instrumentation, Micrometer
- **API Docs**: SpringDoc OpenAPI 2.x
- **Validation**: Spring Boot Validation (Jakarta Bean Validation)

### Angular SPA
- **Core**: Angular 18 standalone components
- **UI**: Angular Material + CDK
- **HTTP**: Angular HttpClient + interceptors
- **Routing**: Angular Router (lazy loading)
- **BPMN**: BPMN.io (custom bundle)
- **Forms**: Angular Reactive Forms
- **State**: Signals + services (no external state management initially)
