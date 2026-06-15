import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { MatCard, MatCardContent } from '@angular/material/card';

@Component({
  selector: 'bpm-process-list',
  standalone: true,
  imports: [RouterLink, MatIcon, MatButton, MatCard, MatCardContent],
  template: `
    <div class="page">
      <div class="page-header">
        <div>
          <h1>Procesos</h1>
          <p class="subtitle">Define y gestiona tus procesos de negocio</p>
        </div>
        <button mat-flat-button color="primary" routerLink="/designer">
          <mat-icon>add</mat-icon> Nuevo proceso
        </button>
      </div>
      <div class="placeholder-state">
        <mat-icon class="placeholder-icon">account_tree</mat-icon>
        <h3>Módulo de procesos</h3>
        <p>Diseña procesos BPMN, despliega a Camunda y monitorea su ejecución</p>
        <button mat-stroked-button routerLink="/designer">
          <mat-icon>draw</mat-icon> Ir al diseñador
        </button>
      </div>
    </div>
  `,
  styles: [`
    .page { max-width: 960px; }
    .page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; flex-wrap: wrap; gap: 12px; }
    .page-header h1 { font-size: 24px; font-weight: 700; margin-bottom: 4px; }
    .subtitle { color: var(--color-text-secondary); font-size: 14px; }
    .placeholder-state { text-align: center; padding: 80px 24px; background: var(--color-surface); border-radius: var(--radius-md); border: 1px dashed var(--color-border); }
    .placeholder-icon { font-size: 56px; width: 56px; height: 56px; color: var(--color-text-disabled); margin-bottom: 16px; }
    .placeholder-state h3 { font-size: 18px; font-weight: 600; margin-bottom: 8px; }
    .placeholder-state p { color: var(--color-text-secondary); margin-bottom: 24px; font-size: 14px; max-width: 400px; margin-left: auto; margin-right: auto; }
  `]
})
export class ProcessListComponent {}
