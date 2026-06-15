# AUDITORÍA
Implementar auditoría completa.
Auditar:
* Procesos
* Versiones
* Documentos
* Instancias
* Metadatos
* Usuarios
* Permisos
* Tareas
Registrar:
* Usuario
* Fecha
* Acción
* Entidad
Auditoría solo lectura.
Mantener trazabilidad completa:
Proceso
→ Instancia
→ Tarea
→ Usuario
→ Documento
→ Metadato
---
# SNAPSHOTS
Toda instancia debe congelar:
* Proceso
* Documento
* Bloques
* Metadatos
* Reglas
* Configuraciones
Nunca consultar versiones activas durante ejecución.
---
# NOTIFICACIONES
Sistema interno.
Arquitectura basada en eventos.
Preparar para:
* Email
* Teams
* WhatsApp
