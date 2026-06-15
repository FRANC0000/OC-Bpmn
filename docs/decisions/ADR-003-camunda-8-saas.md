# ADR-003: Camunda 8 SaaS

**Status**: Accepted
**Date**: 2026-06-15

## Context
We need a workflow engine for BPMN process execution. Options: Camunda 7 (self-hosted embedded/remote) or Camunda 8 (SaaS/self-hosted Zeebe).

## Decision
Use **Camunda 8 SaaS**:
- Zeebe client for process deployment and job completion
- Operate for process monitoring and troubleshooting
- Tasklist for human task management (or custom via Zeebe API)
- No local engine — reduces infrastructure complexity

## Consequences
- No operational overhead for the workflow engine
- Pay-per-use pricing model
- Network latency to Zeebe (mitigated by client-side retry and async patterns)
- Process execution is decoupled from the monolith (future-ready for microservices)
