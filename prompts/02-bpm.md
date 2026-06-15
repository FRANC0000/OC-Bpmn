# BPM DESIGNER
Utilizar BPMN.io.
Construir una versión personalizada.
Modificar:
* Paleta visual
* Context Pad
* Tool Palette
* Theming
Soportar branding por tenant.
Modos:
* Light
* Dark
* Corporate
* Custom
---
# ELEMENTOS BPMN SOPORTADOS
Permitir únicamente:
* Start Event
* End Event
* User Task
* Business Rule Task
* Message Event
* Timer Event
* Parallel Gateway
* Pool / Participant
* Loop Marker
No permitir otros elementos BPMN inicialmente.
Diseñar framework extensible para futuras capacidades.
---
# PLANTILLAS BPM
Soportar:
* Plantillas de proceso reutilizables
* Versionamiento
* Publicación
Ejemplos:
* Aprobación simple
* Aprobación multinivel
* Compras
* Contratación
---
# PROCESOS
Estados:
* Draft
* Active
* Inactive
Versionamiento obligatorio.
Versionado semántico:
* 1.0.0
* 1.1.0
* 2.0.0
Las instancias existentes continúan utilizando la versión original.
Nunca migrar automáticamente instancias activas.
---
# PROCESS OWNER
Implementar concepto:
* Dueño del proceso
Responsable de:
* KPI
* Gobierno
* Publicación
* Ciclo de vida
---
# VARIABLES BPM
Los procesos poseen:
* Variables propias
* Variables documentales
Soportar:
* Variables calculadas
* Variables derivadas
Ejemplos:
* prioridad
* sla
* fechaInicio
* diasTranscurridos
---
# POOLS
Cada Pool representa un Cargo.
Relación:
Pool 1:1 Cargo
Ejemplo:
Pool Finanzas
→ Cargo Finanzas
