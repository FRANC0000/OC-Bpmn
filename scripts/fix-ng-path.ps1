# fix-ng-path.ps1 - Agrega npm global al PATH del usuario (persistente)
# Ejecutar como Administrador: powershell -ExecutionPolicy Bypass -File scripts/fix-ng-path.ps1

$npmPrefix = npm config get prefix 2>$null
if (-not $npmPrefix) {
  $npmPrefix = "$env:USERPROFILE\AppData\Roaming\npm"
}

$currentPath = [Environment]::GetEnvironmentVariable("PATH", "User")
if ($currentPath -notlike "*$npmPrefix*") {
  $newPath = "$npmPrefix;$currentPath"
  [Environment]::SetEnvironmentVariable("PATH", $newPath, "User")
  Write-Host "✓ Agregado $npmPrefix al PATH de usuario"
} else {
  Write-Host "✓ $npmPrefix ya está en el PATH"
}

# También agregar a la PATH de la sesión actual
$env:PATH = "$npmPrefix;$env:PATH"

# Verificar
Write-Host ""
Write-Host "node: $(node --version)"
Write-Host "npm:  $(npm --version)"
Write-Host "ng:   $((ng version --short 2>$null) ?? 'NO INSTALADO')"

if (Get-Command ng -ErrorAction SilentlyContinue) {
  Write-Host ""
  Write-Host "✅ ng está listo. Ejecuta en angular-bpm/: npm install y luego ng serve"
}
