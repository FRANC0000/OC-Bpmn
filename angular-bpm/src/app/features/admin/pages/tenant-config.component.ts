import { Component } from '@angular/core';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'bpm-tenant-config',
  standalone: true,
  imports: [MatIcon],
  template: `
    <div class="page">
      <div class="page-header">
        <div>
          <h1>Tenants</h1>
          <p class="subtitle">Configuración de inquilinos y planes</p>
        </div>
      </div>
      <div class="placeholder-state">
        <mat-icon class="placeholder-icon">business</mat-icon>
        <h3>Configuración de tenants</h3>
        <p>Administra los inquilinos, planes y esquemas de base de datos</p>
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
export class TenantConfigComponent {}
