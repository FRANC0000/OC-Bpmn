import { Component } from '@angular/core';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'bpm-user-management',
  standalone: true,
  imports: [MatIcon],
  template: `
    <div class="page">
      <div class="page-header">
        <div>
          <h1>Usuarios</h1>
          <p class="subtitle">Gestiona los usuarios de la plataforma</p>
        </div>
      </div>
      <div class="placeholder-state">
        <mat-icon class="placeholder-icon">people</mat-icon>
        <h3>Gestión de usuarios</h3>
        <p>Administra usuarios, roles y permisos del sistema</p>
      </div>
    </div>
  `,
  styles: [`
    .page { max-width: 960px; }
    .page-header { margin-bottom: 24px; }
    .page-header h1 { font-size: 24px; font-weight: 700; margin-bottom: 4px; }
    .subtitle { color: var(--color-text-secondary); font-size: 14px; }
    .placeholder-state { text-align: center; padding: 80px 24px; background: var(--color-surface); border-radius: var(--radius-md); border: 1px dashed var(--color-border); }
    .placeholder-icon { font-size: 56px; width: 56px; height: 56px; color: var(--color-text-disabled); margin-bottom: 16px; }
    .placeholder-state h3 { font-size: 18px; font-weight: 600; margin-bottom: 8px; }
    .placeholder-state p { color: var(--color-text-secondary); font-size: 14px; }
  `]
})
export class UserManagementComponent {}
