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
