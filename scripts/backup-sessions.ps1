param()

$env:MISE_PWSH_CHPWD_WARNING = 0
$ErrorActionPreference = "Stop"

$backupDir = [System.IO.Path]::GetFullPath([System.IO.Path]::Combine($PSScriptRoot, "..", "sessions-backup"))
$null = New-Item -ItemType Directory -Force -Path $backupDir

Write-Host "Listing sessions..." -ForegroundColor Cyan
$sessionsJson = opencode session list --format json
$sessions = $sessionsJson | ConvertFrom-Json

Write-Host "Found $($sessions.Count) session(s)" -ForegroundColor Cyan

$rows = @()
foreach ($s in $sessions) {
    $file = [System.IO.Path]::Combine($backupDir, "$($s.id).json")
    Write-Host "  Exporting $($s.id) - $($s.title)" -ForegroundColor Gray
    $jsonOutput = opencode export $s.id
    $jsonOutput | Out-File -FilePath $file -Encoding utf8

    $created = [System.DateTimeOffset]::FromUnixTimeMilliseconds($s.created).DateTime.ToString("yyyy-MM-dd HH:mm")
    $updated = [System.DateTimeOffset]::FromUnixTimeMilliseconds($s.updated).DateTime.ToString("yyyy-MM-dd HH:mm")

    $rows += [PSCustomObject]@{
        ID      = $s.id
        Title   = $s.title
        Created = $created
        Updated = $updated
        File    = "$($s.id).json"
    }
}

Write-Host "Generating BITACORA.md..." -ForegroundColor Cyan

$bitacoraPath = [System.IO.Path]::Combine($backupDir, "BITACORA.md")
$dateStr = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
$hostname = hostname

$lines = @(
    "# Bitacora de Sesiones - OpenCode"
    ""
    "Generado: $dateStr"
    ""
    "| ID | Titulo | Creado | Actualizado | Archivo |"
    "|----|--------|--------|-------------|---------|"
)

foreach ($r in $rows) {
    $lines += "| $($r.ID) | $($r.Title) | $($r.Created) | $($r.Updated) | $($r.File) |"
}

$lines += ""
$lines += "---"
$lines += ""
$lines += "**Total:** $($rows.Count) sesion(es)"
$lines += "**Origen:** $hostname"
$lines += "**Comando:** backup-sessions.ps1"

$lines | Out-File -FilePath $bitacoraPath -Encoding utf8

Write-Host "Committing and pushing to GitHub..." -ForegroundColor Cyan

$repoRoot = Split-Path -Parent $PSScriptRoot
Push-Location $repoRoot
try {
    git add "sessions-backup/"
    git commit -m "backup: sesiones OpenCode ($($rows.Count) sesion(es))"
    git push
} finally {
    Pop-Location
}

Write-Host "Done. $($rows.Count) sesion(es) exportada(s) a sessions-backup/" -ForegroundColor Green
