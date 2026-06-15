param(
    [switch]$SkipTests,
    [switch]$SkipAngular
)

$root = Split-Path -Parent $PSScriptRoot
Write-Host "=== Construyendo BPM Platform ===" -ForegroundColor Cyan

# 1. Build Backend
Write-Host "`n[1/2] Compilando backend (Maven)..." -ForegroundColor Yellow
$mvnArgs = "clean", "package"
if ($SkipTests) { $mvnArgs += "-DskipTests" }
& "mvn" $mvnArgs -f "$root\bpm-platform\pom.xml"
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Fallo la compilacion del backend" -ForegroundColor Red
    exit 1
}
Write-Host "Backend compilado OK" -ForegroundColor Green

# 2. Build Frontend
if (-not $SkipAngular) {
    Write-Host "`n[2/2] Compilando frontend (Angular)..." -ForegroundColor Yellow
    & "npm" run build --prefix "$root\angular-bpm"
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR: Fallo la compilacion del frontend" -ForegroundColor Red
        exit 1
    }
    Write-Host "Frontend compilado OK" -ForegroundColor Green
}

Write-Host "`n=== Build completo ===" -ForegroundColor Cyan
Write-Host "Ejecuta 'docker compose up' para levantar el entorno" -ForegroundColor Green
