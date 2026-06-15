# TAREAS
Tipos:
* User Task
* Automatic Task
---
# USER TASK
Cada User Task debe soportar configuración de:
* Documento asociado
* Versión documento
* Cargo responsable
* Usuario responsable
* Prioridad
* Tiempo esperado
* Tiempo máximo
* Comentarios
* Adjuntos
* Firma
* Acciones disponibles
Acciones:
* Aprobar
* Rechazar
* Solicitar Corrección
Configurables por tarea.
---
# ASIGNACIÓN
Modo 1
Usuario específico.
Modo 2
Cargo.
La tarea llega a una bandeja compartida.
El primer usuario que la toma queda asignado.
Implementar Claim Task.
---
# SLA
Diseñar framework para:
* Tiempo esperado
* Tiempo máximo
* Escalamiento
Preparar arquitectura para:
* Notificaciones
* Reasignación
* Escalamiento jerárquico
---
# INBOX
Implementar desde la primera versión.
Módulos:
* Mis tareas
* Disponibles
* Delegadas
* Subrogadas
* Completadas
* Favoritos
* Historial
Filtros:
* Fecha
* Estado
* Proceso
* Cargo
* Usuario
---
# DELEGACIÓN
Usuario A delega tareas a Usuario B.
Temporal.
---
# SUBROGACIÓN
Usuario B reemplaza oficialmente a Usuario A.
Debe impactar:
* Inbox
* Asignación
* Auditoría
* Notificaciones
