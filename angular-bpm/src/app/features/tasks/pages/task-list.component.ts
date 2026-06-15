import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatCard, MatCardContent } from '@angular/material/card';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'bpm-dashboard',
  standalone: true,
  imports: [RouterLink, MatIcon, MatCard, MatCardContent],
  template: `
    <div class="dashboard">
      <div class="page-header">
        <div>
          <h1>Bienvenido, {{ auth.currentUser()?.displayName }}</h1>
          <p class="subtitle">Panel de control de la plataforma BPM</p>
        </div>
      </div>

      <div class="stats-grid">
        <a routerLink="/documents" class="stat-card">
          <mat-icon class="stat-icon docs">description</mat-icon>
          <div class="stat-info">
            <span class="stat-value">Documentos</span>
            <span class="stat-label">Gestiona tus documentos</span>
          </div>
          <mat-icon class="chevron">chevron_right</mat-icon>
        </a>
        <a routerLink="/processes" class="stat-card">
          <mat-icon class="stat-icon procs">account_tree</mat-icon>
          <div class="stat-info">
            <span class="stat-value">Procesos</span>
            <span class="stat-label">Diseña y ejecuta procesos</span>
          </div>
          <mat-icon class="chevron">chevron_right</mat-icon>
        </a>
        <a routerLink="/designer" class="stat-card">
          <mat-icon class="stat-icon design">draw</mat-icon>
          <div class="stat-info">
            <span class="stat-value">Diseñador</span>
            <span class="stat-label">Editor BPMN</span>
          </div>
          <mat-icon class="chevron">chevron_right</mat-icon>
        </a>
        <a routerLink="/admin" class="stat-card">
          <mat-icon class="stat-icon admin">admin_panel_settings</mat-icon>
          <div class="stat-info">
            <span class="stat-value">Administración</span>
            <span class="stat-label">Usuarios, roles, catálogos</span>
          </div>
          <mat-icon class="chevron">chevron_right</mat-icon>
        </a>
      </div>

      <mat-card class="quick-start">
        <mat-card-content>
          <h3>Inicio rápido</h3>
          <div class="quick-actions">
            <a routerLink="/documents" class="action-chip">
              <mat-icon>add</mat-icon> Nuevo documento
            </a>
            <a routerLink="/processes" class="action-chip">
              <mat-icon>add</mat-icon> Nuevo proceso
            </a>
            <a routerLink="/admin/users" class="action-chip">
              <mat-icon>person_add</mat-icon> Invitar usuario
            </a>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .dashboard { max-width: 960px; }
    .page-header { margin-bottom: 32px; }
    .page-header h1 { font-size: 24px; font-weight: 700; margin-bottom: 4px; }
    .subtitle { color: var(--color-text-secondary); font-size: 14px; }
    .stats-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 16px; margin-bottom: 24px; }
    .stat-card { display: flex; align-items: center; gap: 16px; padding: 20px; background: var(--color-surface); border-radius: var(--radius-md); box-shadow: var(--shadow-sm); text-decoration: none; color: inherit; transition: all var(--transition-fast); border: 1px solid var(--color-border); }
    .stat-card:hover { box-shadow: var(--shadow-md); transform: translateY(-2px); border-color: var(--color-primary); }
    .stat-icon { width: 44px; height: 44px; border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; font-size: 22px; }
    .stat-icon.docs { background: #e3f2fd; color: #1565c0; }
    .stat-icon.procs { background: #e8f5e9; color: #2e7d32; }
    .stat-icon.design { background: #fce4ec; color: #c62828; }
    .stat-icon.admin { background: #f3e5f5; color: #7b1fa2; }
    .stat-info { flex: 1; }
    .stat-value { display: block; font-weight: 600; font-size: 15px; }
    .stat-label { display: block; font-size: 12px; color: var(--color-text-secondary); margin-top: 2px; }
    .chevron { color: var(--color-text-disabled); }
    .quick-start { border-radius: var(--radius-md); border: 1px solid var(--color-border); }
    .quick-start mat-card-content { padding: 24px !important; }
    .quick-start h3 { font-size: 16px; font-weight: 600; margin-bottom: 16px; }
    .quick-actions { display: flex; gap: 12px; flex-wrap: wrap; }
    .action-chip { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border-radius: 24px; background: color-mix(in srgb, var(--color-primary) 8%, transparent); color: var(--color-primary); font-size: 13px; font-weight: 500; text-decoration: none; transition: background var(--transition-fast); }
    .action-chip:hover { background: color-mix(in srgb, var(--color-primary) 16%, transparent); }
    .action-chip mat-icon { font-size: 18px; width: 18px; height: 18px; }
    @media (max-width: 480px) { .stats-grid { grid-template-columns: 1fr; } }
  `]
})
export class TaskListComponent {
  auth = inject(AuthService);
}
