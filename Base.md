# CONTEXTO
Actúa como un equipo compuesto por:
* Enterprise Architect
* Solution Architect
* BPM Architect
* Camunda Architect
* BPMN.io Expert
* SaaS Architect
* DDD Expert
* Software Architect
* PostgreSQL Architect
* Angular Architect
* Spring Boot Architect
* Product Owner
* Technical Lead
Necesito diseñar una plataforma SaaS empresarial para modelar, ejecutar y administrar procesos de negocio y documentos dinámicos.
La solución debe tener calidad Enterprise y estar preparada para escalar durante varios años sin requerir rediseños estructurales.
No simplifiques la solución.
Justifica todas las decisiones arquitectónicas.
Utiliza principios:
* DDD
* Clean Architecture
* Hexagonal Architecture
* Event Driven Architecture
* CQRS donde agregue valor
* SOLID
* Modular Monolith First
* Cloud Native Ready
---
# OBJETIVO DEL PRODUCTO
Construir una plataforma SaaS que permita:
* Diseñar procesos BPMN.
* Ejecutar procesos.
* Administrar documentos dinámicos.
* Diseñar formularios dinámicos.
* Gestionar usuarios.
* Gestionar cargos.
* Gestionar permisos.
* Gestionar auditoría.
* Gestionar firmas.
* Gestionar tareas.
* Gestionar notificaciones.
* Gestionar catálogos.
* Gestionar múltiples tenants.
---
# STACK TECNOLÓGICO
Frontend:
* Angular
Backend:
* Spring Boot
Workflow Engine:
* Camunda
Base de datos:
* PostgreSQL
Archivos:
* AWS S3
Observabilidad:
* OpenTelemetry
* Prometheus
* Grafana
Arquitectura:
* SaaS Multi Tenant
---
# MULTI TENANT
Modelo:
Tenant
└── Organizaciones
└── Unidades Organizacionales
Ejemplo:
Tenant
├── Empresa A
│    ├── Finanzas
│    ├── Compras
│    └── RRHH
│
└── Empresa B
├── Finanzas
└── Operaciones
Implementar:
* Tenant Administration
* Tenant Branding
* Tenant Configuration
* Tenant Security
Estrategia recomendada:
* PostgreSQL Schema por Tenant
Diseñar además estrategia futura para soportar:
* Database por Tenant
sin rediseñar la plataforma.
---
# LICENCIAMIENTO
La plataforma será SaaS comercial.
Implementar sistema de planes.
Ejemplo:
Basic
* Máximo usuarios
* Máximo procesos
* Máximo instancias mensuales
Professional
* Límites superiores
Enterprise
* Sin límites o configurables
Diseñar:
* Consumo
* Cuotas
* Renovación mensual
* Control de límites
* Métricas de uso
---
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
---
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
---
# SISTEMA DOCUMENTAL
Construir motor documental independiente del BPM.
---
# DOCUMENTOS
Un documento contiene:
* Nombre
* Código
* Descripción
* Configuración
* Versiones
Estados:
* Draft
* Active
* Inactive
Versionamiento obligatorio.
---
# PLANTILLAS DOCUMENTALES
Soportar:
* Plantillas reutilizables
* Versionamiento
* Publicación
---
# DOCUMENTOS INSTANCIADOS
Generados durante ejecución.
Deben poseer:
* Folio
* Snapshot completo
* Auditoría
---
# FOLIOS
Diseñar servicio independiente.
Ejemplos:
DOC-{YYYY}-{000000}
Configurables.
---
# CONSTRUCTOR DE FORMULARIOS
Experiencia tipo Notion.
Drag & Drop.
Diseñar editor visual.
---
# BLOQUES
Un documento contiene bloques.
Tipos:
* Section
* Columns
* Tabs
* Accordion
* Repeatable Block
* Grid Block
Versionables.
Reutilizables.
Biblioteca central.
---
# GRID BLOCK
NO modelar Grid como metadato.
Grid es un bloque especializado.
Debe soportar:
* Columnas tipadas
* Fórmulas por fila
* Fórmulas por columna
* Totales
* Agregaciones
* Validaciones
* Límite configurable
* Ilimitado configurable
* Virtual Scrolling
* Paginación eficiente
Analizar alternativas de rendimiento.
---
# METADATOS
Tipos:
* Alfanumérico
* Numérico
* Correo
* Fecha
* Hora
* FechaHora
* Lista
* ComboBox
* RadioButton
* CheckBox
* Usuario
* Usuario por Cargo
* Cargo
* Adjunto
* Fórmula
Diseñar framework extensible.
---
# CONFIGURACIÓN DE METADATOS
Cada metadato debe soportar:
* Visible
* Oculto
* Editable
* Solo lectura
* Obligatorio
* Opcional
Además:
* Validaciones
* Expresiones
* Reglas
---
# OVERRIDES POR TAREA
Una tarea puede modificar el comportamiento de los metadatos.
Ejemplo:
Monto:
* Editable
Proveedor:
* Solo lectura
Observación:
* Obligatoria
Sin crear nuevas versiones documentales.
---
# MOTOR DE EXPRESIONES
Implementar un único motor.
Utilizarlo para:
* Fórmulas
* Validaciones
* Visibilidad
* Reglas BPM
* Asignaciones futuras
Sintaxis:
* Estilo Excel
* Expresiones simples
Puede acceder a:
* Metadatos
* Usuarios
Ejemplos:
monto > 1000000
usuario.cargo == "Gerencia"
---
# CATÁLOGOS
Implementar sistema de catálogos.
Niveles:
* Global
* Tenant
* Organización
* Documento
Soportar:
* Jerarquías
* Dependencias
Ejemplos:
País
→ Región
→ Ciudad
CentroCosto
→ Área
→ SubÁrea
---
# ARCHIVOS
Utilizar AWS S3.
Implementar:
* Versionamiento
* Auditoría
* Seguridad
* Descarga controlada
---
# FIRMA
Soportar:
* Firma simple
* Firma electrónica avanzada
Diseñar arquitectura desacoplada.
Proveedor agnóstico.
Soportar:
* Uno o múltiples firmantes
Una vez firmado:
Documento bloqueado.
Permitir:
Solicitud de excepción.
---
# INTEGRACIONES
Inicialmente:
* REST Consumer
Diseñar framework para:
* Configuración de entradas
* Configuración de salidas
* Mapeo de datos
Preparar arquitectura para futuras integraciones.
---
# SEGURIDAD
Permisos granulares.
Ejemplos:
document.create
document.edit
document.publish
process.create
process.publish
metadata.manage
users.manage
audit.view
dashboard.view
Cargo y Rol se consideran equivalentes en esta versión.
---
# USUARIOS
Cada usuario puede tener:
* Cargo principal
* Múltiples cargos secundarios
Estados:
* Activo
* Suspendido
* Bloqueado
* Eliminado lógico
---
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
---
# DASHBOARD
KPIs:
* Procesos iniciados
* Procesos terminados
* Procesos atrasados
* SLA
* Productividad por cargo
* Productividad por usuario
---
# MULTIIDIOMA
Idiomas base:
* Español
* Inglés
Preparar framework extensible.
Soportar:
* Traducciones
* Formatos regionales
* Configuración por tenant
---
# OBSERVABILIDAD
Implementar:
* Logs
* Métricas
* Trazas
Utilizar:
* OpenTelemetry
* Prometheus
* Grafana
Diseñar dashboard operacional.
---
# IA FUTURA
Diseñar bounded context independiente.
No implementar ahora.
Preparar para:
* Generación de formularios
* Generación BPMN
* Resumen de casos
* Asistente operacional
---
# AMBIENTES
Soportar:
* Desarrollo
* QA
* Producción
Implementar:
* Exportación
* Importación
* Promoción
de:
* Procesos
* Documentos
* Catálogos
---
# ENTREGABLES
Genera la solución en etapas.
ETAPA 1
* Arquitectura General
* Arquitectura SaaS
* C4
* Bounded Contexts
ETAPA 2
* DDD
* Agregados
* Entidades
* Eventos de dominio
* Casos de uso
ETAPA 3
* Modelo de datos PostgreSQL
* Estrategia Multi Tenant
* Índices
* Performance
ETAPA 4
* BPM Engine
* BPMN.io
* Camunda
* Ejecución
* Versionamiento
ETAPA 5
* Document Engine
* Formularios
* Bloques
* Grid
* Metadatos
* Catálogos
ETAPA 6
* Seguridad
* Usuarios
* Auditoría
* Notificaciones
ETAPA 7
* Angular
* Componentes
* Módulos
* UX
ETAPA 8
* APIs
* Contratos
* DTOs
ETAPA 9
* Observabilidad
* Operación
* DevOps
ETAPA 10
* Roadmap MVP
* Roadmap Enterprise
* Riesgos
* Estrategia de evolución
No avances a la siguiente etapa sin finalizar completamente la actual.