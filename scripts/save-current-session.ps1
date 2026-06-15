param()

$env:MISE_PWSH_CHPWD_WARNING = 0
$ErrorActionPreference = "Stop"

$backupDir = [System.IO.Path]::GetFullPath([System.IO.Path]::Combine($PSScriptRoot, "..", "sessions-backup"))
$null = New-Item -ItemType Directory -Force -Path $backupDir

Write-Host "Looking for the latest session..." -ForegroundColor Cyan
$sessionsJson = opencode session list --format json
$sessions = $sessionsJson | ConvertFrom-Json

if ($sessions.Count -eq 0) {
    Write-Host "No sessions found." -ForegroundColor Red
    exit 1
}

$latest = $sessions | Sort-Object -Property updated -Descending | Select-Object -First 1

$file = [System.IO.Path]::Combine($backupDir, "$($latest.id).json")
Write-Host "Saving session: $($latest.title)" -ForegroundColor Cyan
$jsonOutput = opencode export $latest.id
$jsonOutput | Out-File -FilePath $file -Encoding utf8

$created = [System.DateTimeOffset]::FromUnixTimeMilliseconds($latest.created).DateTime.ToString("yyyy-MM-dd HH:mm")
$updated = [System.DateTimeOffset]::FromUnixTimeMilliseconds($latest.updated).DateTime.ToString("yyyy-MM-dd HH:mm")

Write-Host "Updating BITACORA.md..." -ForegroundColor Cyan
$bitacoraPath = [System.IO.Path]::Combine($backupDir, "BITACORA.md")
$dateStr = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
$hostname = hostname

$separator = "---"
$header = @(
    "# Bitacora de Sesiones - OpenCode"
    ""
    "Generado: $dateStr"
    ""
    "| ID | Titulo | Creado | Actualizado | Archivo |"
    "|----|--------|--------|-------------|---------|"
)

if (Test-Path $bitacoraPath) {
    $existing = Get-Content $bitacoraPath
    $inTable = $false
    $existingRows = @()
    foreach ($line in $existing) {
        if ($line -match '^\|----\|') { $inTable = $true; continue }
        if ($inTable) {
            if ($line -match '^\|') { $existingRows += $line }
            elseif ($line -eq $separator) { $inTable = $false }
        }
    }
    $newRow = "| $($latest.id) | $($latest.title) | $created | $updated | $($latest.id).json |"
    $existingRows = @($existingRows | Where-Object { $_ -notmatch [regex]::Escape($latest.id) })
    $existingRows = @($existingRows) + @($newRow)

    $header | Out-File -FilePath $bitacoraPath -Encoding utf8
    $existingRows | Out-File -FilePath $bitacoraPath -Encoding utf8 -Append
    "" | Out-File -FilePath $bitacoraPath -Encoding utf8 -Append
    $separator | Out-File -FilePath $bitacoraPath -Encoding utf8 -Append
    "" | Out-File -FilePath $bitacoraPath -Encoding utf8 -Append
    "**Total:** $($existingRows.Count) sesion(es)" | Out-File -FilePath $bitacoraPath -Encoding utf8 -Append
    "**Origen:** $hostname" | Out-File -FilePath $bitacoraPath -Encoding utf8 -Append
    "**Comando:** save-current-session.ps1" | Out-File -FilePath $bitacoraPath -Encoding utf8 -Append
} else {
    $lines = $header
    $lines += "| $($latest.id) | $($latest.title) | $created | $updated | $($latest.id).json |"
    $lines += ""
    $lines += $separator
    $lines += ""
    $lines += "**Total:** 1 sesion(es)"
    $lines += "**Origen:** $hostname"
    $lines += "**Comando:** save-current-session.ps1"
    $lines | Out-File -FilePath $bitacoraPath -Encoding utf8
}

Write-Host "Committing and pushing to GitHub..." -ForegroundColor Cyan
Push-Location (Split-Path -Parent $PSScriptRoot)
try {
    git add "sessions-backup/"
    git commit -m "backup: sesion actual ($($latest.title))"
    git push
} finally {
    Pop-Location
}

Write-Host "Done. Session saved: $($latest.id) - $($latest.title)" -ForegroundColor Green
Write-Host "To restore on another PC: opencode import $file" -ForegroundColor Green
