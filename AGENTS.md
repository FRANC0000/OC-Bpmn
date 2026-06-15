# Sesiones Multi-PC

Las sesiones de OpenCode viajan entre PCs via GitHub.

## Salvar sesion actual

Cuando el usuario pida salvar/guardar la sesion actual, o mencione cambiar de PC:

1. Ejecuta `scripts/save-current-session.ps1` con PowerShell
2. El script exporta la sesion mas reciente, actualiza `sessions-backup/BITACORA.md`, hace commit y push

## Restaurar en otro PC

Clonar repo + importar la sesion desde `sessions-backup/`:

```powershell
git clone https://github.com/FRANC0000/OC-Bpmn
cd OC-Bpmn
opencode import sessions-backup/<session-id>.json
```
