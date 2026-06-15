# BPM Platform

SaaS Enterprise platform for modeling, executing, and managing business processes and dynamic documents.

## Stack

| Layer | Technology |
|-------|-----------|
| Frontend | Angular 18 (Standalone Components) |
| Backend | Spring Boot 3.3 / Java 21 |
| Workflow Engine | Camunda 8 (SaaS) |
| Database | PostgreSQL 16 |
| Storage | AWS S3 |
| Observability | OpenTelemetry, Prometheus, Grafana |

## Architecture

- **Modular Monolith** — single deployable unit with bounded context modules
- **DDD + Hexagonal** — domain-driven design inside each context
- **Event Driven** — domain events for cross-context communication
- **CQRS** — where value is demonstrated
- **Multi-tenant** — schema-per-tenant strategy (PostgreSQL)

## Project Structure

```
bpm-platform/          # Spring Boot multi-module (Maven)
├── bpm-common/        # DDD seedwork, shared kernel
├── bpm-tenant/        # Tenant bounded context
├── bpm-process/       # Process bounded context
├── bpm-document/      # Document engine bounded context
├── bpm-security/      # Security bounded context
├── bpm-api/           # REST API entry point
└── bpm-infrastructure/ # Persistence, messaging, clients

angular-bpm/           # Angular 18 standalone frontend
└── src/app/
    ├── core/          # Auth, HTTP, guards
    ├── shared/        # Reusable components
    ├── features/      # Feature modules (lazy)
    └── layouts/       # App shell layouts
```

## Prerequisites

- Java 21
- Node.js 20+ / npm
- Docker Desktop
- Maven 3.9+

## Quick Start

```bash
# Start infrastructure
docker compose up -d

# Build backend
cd bpm-platform
mvn clean install -DskipTests

# Start backend
mvn spring-boot:run -pl bpm-api -Dspring-boot.run.profiles=local

# (in another terminal) Start frontend
cd angular-bpm
npm install
ng serve
```

## License

Commercial SaaS product.
