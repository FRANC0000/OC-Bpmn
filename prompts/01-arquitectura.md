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
