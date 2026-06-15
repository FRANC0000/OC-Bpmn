# C4 Context Diagram — Nivel 1

```
┌────────────────────────────────────────────────────────────────────┐
│                        BPM PLATFORM (SaaS)                         │
│                                                                     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌───────────────────┐  │
│  │  Angular  │  │  Spring  │  │Camunda 8 │  │   PostgreSQL      │  │
│  │  (SPA)    │◄─┤  Boot    │◄─┤  (SaaS)  │  │   Schema/Tenant   │  │
│  │           │──►│  API     │  │  Zeebe   │  │                   │  │
│  └──────────┘  └────┬─────┘  └──────────┘  └───────────────────┘  │
│                     │                                              │
│                     ▼                                              │
│           ┌────────────────────────┐                              │
│           │      AWS S3            │                              │
│           │  (File Storage)        │                              │
│           └────────────────────────┘                              │
└────────────────────────────────────────────────────────────────────┘
         │                    │                 │
         ▼                    ▼                 ▼
┌──────────────┐   ┌────────────────┐  ┌────────────────┐
│   Usuario    │   │  Administrador │  │  Sistema       │
│   Operativo  │   │  de Tenant     │  │  Externo (REST)│
└──────────────┘   └────────────────┘  └────────────────┘
```

## Personas

| Persona | Descripción |
|---------|-------------|
| **Usuario Operativo** | Ejecuta tareas, completa formularios, firma documentos |
| **Administrador de Tenant** | Configura procesos, documentos, usuarios y permisos |
| **Sistema Externo** | Consume APIs REST para integraciones |

## Systems

| System | Description |
|--------|-------------|
| **Angular SPA** | Frontend de una sola página, standalone components, lazy loading |
| **Spring Boot API** | Backend modular con bounded contexts (monolito modular) |
| **Camunda 8** | Workflow engine SaaS - Zeebe para orquestación de procesos |
| **PostgreSQL** | Base de datos relacional multi-tenant (schema per tenant) |
| **AWS S3** | Almacenamiento de archivos con versioning y auditoría |
