import { Component, inject } from '@angular/core';
import { Location } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'bpm-designer',
  standalone: true,
  imports: [MatIcon, MatButton],
  template: `
    <div class="designer-page">
      <div class="designer-header">
        <button mat-stroked-button (click)="location.back()">
          <mat-icon>arrow_back</mat-icon> Volver
        </button>
        <span class="designer-title">Diseñador BPMN</span>
        <div class="designer-actions">
          <button mat-stroked-button disabled>
            <mat-icon>save</mat-icon> Guardar
          </button>
          <button mat-flat-button color="primary" disabled>
            <mat-icon>publish</mat-icon> Publicar
          </button>
        </div>
      </div>
      <div class="designer-canvas">
        <div class="placeholder">
          <mat-icon>draw</mat-icon>
          <h3>Diseñador de procesos</h3>
          <p>El editor BPMN estará disponible cuando se integre bpmn-js</p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .designer-page { display: flex; flex-direction: column; height: calc(100vh - var(--header-height) - 48px); margin: -24px; }
    .designer-header { display: flex; align-items: center; gap: 12px; padding: 12px 24px; background: var(--color-surface); border-bottom: 1px solid var(--color-border); }
    .designer-title { flex: 1; font-weight: 600; font-size: 15px; }
    .designer-actions { display: flex; gap: 8px; }
    .designer-canvas { flex: 1; display: flex; align-items: center; justify-content: center; background: var(--color-bg); }
    .placeholder { text-align: center; color: var(--color-text-secondary); }
    .placeholder mat-icon { font-size: 64px; width: 64px; height: 64px; color: var(--color-text-disabled); margin-bottom: 16px; }
    .placeholder h3 { font-size: 18px; font-weight: 600; color: var(--color-text); margin-bottom: 8px; }
    @media (max-width: 767px) { .designer-page { margin: -16px; } .designer-header { padding: 12px 16px; flex-wrap: wrap; } .designer-title { order: -1; width: 100%; } }
  `]
})
export class DesignerComponent {
  location = inject(Location);
}
