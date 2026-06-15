#!/usr/bin/env bash
# setup-git-bash.sh - Configura Git Bash para reconocer ng y otros comandos npm globales
# Ejecutar: source scripts/setup-git-bash.sh  (o . scripts/setup-git-bash.sh)

NPM_PREFIX=$(npm config get prefix 2>/dev/null)
if [ -z "$NPM_PREFIX" ]; then
  NPM_PREFIX="$HOME/AppData/Roaming/npm"
fi

# Agregar npm global al PATH si no está
if [[ ":$PATH:" != *":$NPM_PREFIX:"* ]]; then
  export PATH="$NPM_PREFIX:$PATH"
  echo "✓ Agregado $NPM_PREFIX al PATH"
fi

# Alias para ng en caso de que ng.cmd no sea detectado
if ! command -v ng &> /dev/null; then
  if [ -f "$NPM_PREFIX/ng" ]; then
    alias ng="$NPM_PREFIX/ng"
    echo "✓ Alias ng creado"
  elif [ -f "$NPM_PREFIX/ng.cmd" ]; then
    alias ng="$NPM_PREFIX/ng.cmd"
    echo "✓ Alias ng creado (via ng.cmd)"
  fi
fi

# Verificar
echo ""
echo "node: $(node --version 2>/dev/null || echo 'NO INSTALADO')"
echo "npm:  $(npm --version 2>/dev/null || echo 'NO INSTALADO')"
echo "ng:   $(ng version --short 2>/dev/null || echo 'NO INSTALADO')"

if command -v ng &> /dev/null; then
  echo ""
  echo "✅ ng está listo para usar. Ejecuta: ng serve"
fi
