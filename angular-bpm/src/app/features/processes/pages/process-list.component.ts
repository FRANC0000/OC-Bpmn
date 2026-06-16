import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { ApiResponse } from '../../../core/models/document.model';

interface ProcessSummary {
  id: string;
  name: string;
  slug: string;
  description: string;
  status: string;
  versionCount: number;
  bpmnXml: string | null;
}

@Component({
  selector: 'bpm-process-list',
  standalone: true,
  imports: [RouterLink, MatIcon, MatButtonModule, MatTableModule],
  template: `
    <div class="page">
      <div class="page-header">
        <div>
          <h1>Procesos</h1>
          <p class="subtitle">Define y gestiona tus procesos de negocio</p>
        </div>
        <button mat-flat-button color="primary" routerLink="/designer/new">
          <mat-icon>add</mat-icon> Nuevo proceso
        </button>
      </div>

      @if (loading()) {
        <div class="loading">Cargando procesos...</div>
      } @else if (processes().length === 0) {
        <div class="placeholder-state">
          <mat-icon class="placeholder-icon">account_tree</mat-icon>
          <h3>Sin procesos aún</h3>
          <p>Crea tu primer proceso de negocio</p>
          <button mat-stroked-button routerLink="/designer/new">
            <mat-icon>add</mat-icon> Crear proceso
          </button>
        </div>
      } @else {
        <div class="table-container">
          <table mat-table [dataSource]="processes()" class="data-table">
            <ng-container matColumnDef="name">
              <th mat-header-cell *matHeaderCellDef>Nombre</th>
              <td mat-cell *matCellDef="let p">{{ p.name }}</td>
            </ng-container>

            <ng-container matColumnDef="slug">
              <th mat-header-cell *matHeaderCellDef>Slug</th>
              <td mat-cell *matCellDef="let p">{{ p.slug }}</td>
            </ng-container>

            <ng-container matColumnDef="status">
              <th mat-header-cell *matHeaderCellDef>Estado</th>
              <td mat-cell *matCellDef="let p">
                <span class="status-badge" [class.active]="p.status === 'active'">{{ p.status }}</span>
              </td>
            </ng-container>

            <ng-container matColumnDef="versions">
              <th mat-header-cell *matHeaderCellDef>Versiones</th>
              <td mat-cell *matCellDef="let p">{{ p.versionCount }}</td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef></th>
              <td mat-cell *matCellDef="let p">
                <button mat-stroked-button [routerLink]="['/designer', p.id]">
                  <mat-icon>draw</mat-icon> Diseñar
                </button>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="columns"></tr>
            <tr mat-row *matRowDef="let row; columns: columns;"></tr>
          </table>
        </div>
      }
    </div>
  `,
  styles: [`
    .page { max-width: 960px; }
    .page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; flex-wrap: wrap; gap: 12px; }
    .page-header h1 { font-size: 24px; font-weight: 700; margin-bottom: 4px; }
    .subtitle { color: var(--color-text-secondary); font-size: 14px; }
    .loading { text-align: center; padding: 48px; color: var(--color-text-secondary); }
    .table-container { background: var(--color-surface); border-radius: var(--radius-md); overflow: hidden; box-shadow: var(--shadow-sm); }
    .placeholder-state { text-align: center; padding: 80px 24px; background: var(--color-surface); border-radius: var(--radius-md); border: 1px dashed var(--color-border); }
    .placeholder-icon { font-size: 56px; width: 56px; height: 56px; color: var(--color-text-disabled); margin-bottom: 16px; }
    .placeholder-state h3 { font-size: 18px; font-weight: 600; margin-bottom: 8px; }
    .placeholder-state p { color: var(--color-text-secondary); margin-bottom: 24px; }
  `]
})
export class ProcessListComponent implements OnInit {
  private http = inject(HttpClient);
  private snackBar = inject(MatSnackBar);
  private api = environment.apiUrl;

  processes = signal<ProcessSummary[]>([]);
  loading = signal(true);
  columns = ['name', 'slug', 'status', 'versions', 'actions'];

  ngOnInit() {
    this.http.get<ApiResponse<ProcessSummary[]>>(`${this.api}/processes`).subscribe({
      next: (r) => {
        if (r.success) this.processes.set(r.data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.snackBar.open('Error al cargar procesos', 'Cerrar', { duration: 4000 });
      }
    });
  }
}
