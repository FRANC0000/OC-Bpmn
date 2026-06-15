param(
    [switch]$SkipTests,
    [switch]$SkipBuild,
    [switch]$Detach
)

$root = Split-Path -Parent $PSScriptRoot

if (-not $SkipBuild) {
    & "$PSScriptRoot\build-all.ps1" -SkipTests:$SkipTests
    if ($LASTEXITCODE -ne 0) { exit 1 }
}

Write-Host "`n=== Levantando entorno Docker ===" -ForegroundColor Cyan
$dockerArgs = @("compose", "--env-file", "$root\.env", "up", "--build")
if ($Detach) { $dockerArgs += "-d" }

& "docker" $dockerArgs -f "$root\docker-compose.yml"

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n=== Entorno listo ===" -ForegroundColor Green
    Write-Host "Frontend: http://localhost" -ForegroundColor Cyan
    Write-Host "Backend:  http://localhost:8080" -ForegroundColor Cyan
    Write-Host "pgAdmin:  http://localhost:5050 (admin@bpmplatform.com / admin)" -ForegroundColor Cyan
    Write-Host "Swagger:  http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
}
