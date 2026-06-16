import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { Router } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';
import { AdminService } from '../../../core/services/admin.service';
import { TenantSummary } from '../../../core/models/admin.model';

@Component({
  selector: 'bpm-tenant-config',
  standalone: true,
  imports: [
    MatIcon, MatTableModule, MatButtonModule, MatButtonToggleModule, MatChipsModule, NgClass,
    MatFormFieldModule, MatInputModule, MatSelectModule, MatTooltipModule, FormsModule
  ],
  template: `
    <div class="page">
      <div class="page-header">
        <div>
          <h1>Tenants</h1>
          <p class="subtitle">Gestiona los inquilinos del sistema</p>
        </div>
        <button mat-flat-button color="primary" (click)="showCreateForm = !showCreateForm">
          <mat-icon>add</mat-icon>
          {{ showCreateForm ? 'Cancelar' : 'Nuevo tenant' }}
        </button>
      </div>

      @if (showCreateForm) {
        <div class="form-card">
          <h3>Nuevo inquilino</h3>
          <div class="form-grid">
            <mat-form-field appearance="outline">
              <mat-label>Nombre</mat-label>
              <input matInput [(ngModel)]="createForm.name" placeholder="Mi Empresa">
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Slug</mat-label>
              <input matInput [(ngModel)]="createForm.slug" placeholder="mi-empresa">
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Plan</mat-label>
              <mat-select [(ngModel)]="createForm.planCode">
                <mat-option value="basic">Basic</mat-option>
                <mat-option value="professional">Professional</mat-option>
                <mat-option value="enterprise">Enterprise</mat-option>
              </mat-select>
            </mat-form-field>
          </div>
          <div class="form-actions">
            <button mat-flat-button color="primary" (click)="createTenant()" [disabled]="!createForm.name || !createForm.slug">
              <mat-icon>business</mat-icon>
              Crear tenant
            </button>
          </div>
        </div>
      }

      <div class="filter-bar">
        <mat-button-toggle-group [(ngModel)]="statusFilter" (change)="onFilterChange()" aria-label="Filtrar por estado">
          <mat-button-toggle value="all">Todos</mat-button-toggle>
          <mat-button-toggle value="active">Activos</mat-button-toggle>
          <mat-button-toggle value="suspended">Suspendidos</mat-button-toggle>
        </mat-button-toggle-group>
      </div>

      @if (loading) {
        <div class="loading-state">
          <mat-icon class="spin">refresh</mat-icon>
          <p>Cargando tenants...</p>
        </div>
      } @else {
        <div class="table-container">
          <table mat-table [dataSource]="filteredTenants()" class="data-table">
            <ng-container matColumnDef="name">
              <th mat-header-cell *matHeaderCellDef>Nombre</th>
              <td mat-cell *matCellDef="let t">
                @if (editingId === t.id) {
                  <mat-form-field appearance="outline" class="inline-edit">
                    <input matInput [(ngModel)]="editName" (keyup.enter)="saveTenant(t)">
                  </mat-form-field>
                } @else {
                  {{ t.name }}
                }
              </td>
            </ng-container>

            <ng-container matColumnDef="slug">
              <th mat-header-cell *matHeaderCellDef>Slug</th>
              <td mat-cell *matCellDef="let t">
                @if (editingId === t.id) {
                  <mat-form-field appearance="outline" class="inline-edit">
                    <input matInput [(ngModel)]="editSlug" (keyup.enter)="saveTenant(t)">
                  </mat-form-field>
                } @else {
                  <code>{{ t.slug }}</code>
                }
              </td>
            </ng-container>

            <ng-container matColumnDef="plan">
              <th mat-header-cell *matHeaderCellDef>Plan</th>
              <td mat-cell *matCellDef="let t">
                <mat-chip-row [class]="'chip-' + t.plan">{{ t.plan }}</mat-chip-row>
              </td>
            </ng-container>

            <ng-container matColumnDef="status">
              <th mat-header-cell *matHeaderCellDef>Estado</th>
              <td mat-cell *matCellDef="let t">
                <span class="status-badge" [class.active]="t.status === 'active'" [class.inactive]="t.status === 'suspended'">
                  {{ t.status }}
                </span>
              </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef></th>
              <td mat-cell *matCellDef="let t">
                @if (editingId === t.id) {
                  <button mat-icon-button color="primary" (click)="saveTenant(t)" matTooltip="Guardar">
                    <mat-icon>check</mat-icon>
                  </button>
                  <button mat-icon-button (click)="cancelEdit()" matTooltip="Cancelar">
                    <mat-icon>close</mat-icon>
                  </button>
                } @else {
                  <button mat-icon-button (click)="startEdit(t)" matTooltip="Editar">
                    <mat-icon>edit</mat-icon>
                  </button>
                  <button mat-icon-button (click)="toggleTenantStatus(t)" [matTooltip]="t.status === 'active' ? 'Suspender' : 'Activar'">
                    <mat-icon>{{ t.status === 'active' ? 'pause_circle' : 'play_circle' }}</mat-icon>
                  </button>
                  <button mat-icon-button color="primary" (click)="selectTenant(t)" matTooltip="Ver usuarios">
                    <mat-icon>visibility</mat-icon>
                  </button>
                }
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="columns"></tr>
            <tr mat-row *matRowDef="let row; columns: columns;"></tr>
          </table>
        </div>

        @if (tenants().length === 0) {
          <div class="empty-state">
            <mat-icon>business</mat-icon>
            <h3>Sin inquilinos</h3>
            <p>No hay tenants registrados en el sistema</p>
          </div>
        }
      }

      @if (error) {
        <div class="error-state">
          <mat-icon>error</mat-icon>
          <p>{{ error }}</p>
        </div>
      }
    </div>
  `,
  styles: [`
    .page { max-width: 1200px; }
    .page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
    .page-header h1 { font-size: 24px; font-weight: 700; margin-bottom: 4px; }
    .subtitle { color: var(--color-text-secondary); font-size: 14px; }
    .form-card { background: var(--color-surface); border-radius: var(--radius-md); padding: 20px; margin-bottom: 24px; border: 1px solid var(--color-border); }
    .form-card h3 { font-size: 16px; font-weight: 600; margin-bottom: 16px; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .form-actions { margin-top: 16px; display: flex; gap: 8px; }
    .filter-bar { margin-bottom: 16px; }
    .table-container { background: var(--color-surface); border-radius: var(--radius-md); overflow: hidden; box-shadow: var(--shadow-sm); }
    .inline-edit { margin: -16px 0; width: 140px; }
    .chip-basic { background: var(--color-primary-container); color: var(--color-on-primary-container); }
    .chip-professional { background: var(--color-secondary-container); color: var(--color-on-secondary-container); }
    .chip-enterprise { background: var(--color-accent); color: var(--color-on-primary); }
    .loading-state, .empty-state, .error-state { text-align: center; padding: 60px 24px; color: var(--color-text-secondary); }
    .loading-state mat-icon, .empty-state mat-icon { font-size: 48px; width: 48px; height: 48px; margin-bottom: 16px; }
    .empty-state h3 { font-size: 18px; font-weight: 600; margin-bottom: 8px; color: var(--color-text); }
    .error-state { color: var(--color-error); }
    .spin { animation: spin 1s linear infinite; }
    @keyframes spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
  `]
})
export class TenantConfigComponent implements OnInit {
  private admin = inject(AdminService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  tenants = signal<TenantSummary[]>([]);
  filteredTenants = computed(() => {
    const all = this.tenants();
    if (this.statusFilter === 'all') return all;
    return all.filter(t => t.status === this.statusFilter);
  });
  loading = true;
  error = '';
  columns = ['name', 'slug', 'plan', 'status', 'actions'];
  showCreateForm = false;
  editingId: string | null = null;
  editName = '';
  editSlug = '';
  statusFilter = 'all';
  createForm = { name: '', slug: '', planCode: 'basic' };

  onFilterChange() {
    // handled by computed signal
  }

  ngOnInit() {
    this.loadTenants();
  }

  private loadTenants() {
    this.loading = true;
    this.error = '';
    this.admin.getTenants().subscribe({
      next: (data) => { this.tenants.set(data); this.loading = false; },
      error: (err) => { console.error('Error loading tenants', err); this.error = 'Error al cargar tenants (' + (err.status || err.message) + ')'; this.loading = false; }
    });
  }

  createTenant() {
    this.admin.createTenant(this.createForm).subscribe({
      next: () => {
        this.snackBar.open('Tenant creado exitosamente', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.showCreateForm = false;
        this.createForm = { name: '', slug: '', planCode: 'basic' };
        this.loadTenants();
      },
      error: (err) => this.snackBar.open(err.error?.message || 'Error al crear tenant', 'Cerrar', { duration: 5000 })
    });
  }

  startEdit(t: TenantSummary) {
    this.editingId = t.id;
    this.editName = t.name;
    this.editSlug = t.slug;
  }

  cancelEdit() {
    this.editingId = null;
  }

  saveTenant(t: TenantSummary) {
    this.admin.updateTenant(t.id, { name: this.editName, slug: this.editSlug }).subscribe({
      next: () => {
        this.snackBar.open('Tenant actualizado', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.editingId = null;
        this.loadTenants();
      },
      error: (err) => this.snackBar.open(err.error?.message || 'Error al actualizar tenant', 'Cerrar', { duration: 5000 })
    });
  }

  toggleTenantStatus(t: TenantSummary) {
    const newStatus = t.status === 'active' ? 'suspended' : 'active';
    this.admin.updateTenantStatus(t.id, { status: newStatus }).subscribe({
      next: () => {
        this.snackBar.open(`Tenant ${newStatus === 'active' ? 'activado' : 'suspendido'}`, '', { duration: 3000, panelClass: 'success-snackbar' });
        this.loadTenants();
      },
      error: (err) => this.snackBar.open(err.error?.message || 'Error al cambiar estado', 'Cerrar', { duration: 5000 })
    });
  }

  selectTenant(t: TenantSummary) {
    this.router.navigate(['/admin/users'], { queryParams: { tenantId: t.id } });
  }
}
