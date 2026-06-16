import { Component, OnInit, inject, signal } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../core/services/admin.service';
import { RoleSummary } from '../../../core/models/admin.model';

@Component({
  selector: 'bpm-role-management',
  standalone: true,
  imports: [
    MatIcon, MatTableModule, MatButtonModule, MatFormFieldModule,
    MatInputModule, MatSelectModule, MatSnackBarModule, MatTooltipModule, FormsModule
  ],
  template: `
    <div class="page">
      <div class="page-header">
        <div>
          <h1>Roles</h1>
          <p class="subtitle">Gestiona los roles del sistema</p>
        </div>
        <button mat-flat-button color="primary" (click)="showCreateForm = !showCreateForm">
          <mat-icon>add</mat-icon>
          {{ showCreateForm ? 'Cancelar' : 'Nuevo rol' }}
        </button>
      </div>

      @if (showCreateForm) {
        <div class="form-card">
          <h3>Nuevo rol</h3>
          <div class="form-grid">
            <mat-form-field appearance="outline">
              <mat-label>Nombre</mat-label>
              <input matInput [(ngModel)]="createForm.name" placeholder="EDITOR">
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Tipo</mat-label>
              <mat-select [(ngModel)]="createForm.roleType">
                <mat-option value="primary">Primary</mat-option>
                <mat-option value="secondary">Secondary</mat-option>
              </mat-select>
            </mat-form-field>
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Descripción</mat-label>
              <input matInput [(ngModel)]="createForm.description" placeholder="Rol editor con permisos de lectura/escritura">
            </mat-form-field>
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Permisos (JSON)</mat-label>
              <textarea matInput [(ngModel)]="createForm.permissions" rows="3" placeholder='["read:users","write:users"]'></textarea>
            </mat-form-field>
          </div>
          <div class="form-actions">
            <button mat-flat-button color="primary" (click)="createRole()" [disabled]="!createForm.name">
              <mat-icon>add</mat-icon>
              Crear rol
            </button>
          </div>
        </div>
      }

      <div class="table-container">
        <table mat-table [dataSource]="roles()" class="data-table">
          <ng-container matColumnDef="name">
            <th mat-header-cell *matHeaderCellDef>Nombre</th>
            <td mat-cell *matCellDef="let r">
              @if (editingId === r.id) {
                <mat-form-field appearance="outline" class="inline-edit">
                  <input matInput [(ngModel)]="editName" (keyup.enter)="saveRole(r)">
                </mat-form-field>
              } @else {
                {{ r.name }}
                @if (r.isSystem) { <mat-icon class="system-icon" matTooltip="Rol del sistema">lock</mat-icon> }
              }
            </td>
          </ng-container>

          <ng-container matColumnDef="description">
            <th mat-header-cell *matHeaderCellDef>Descripción</th>
            <td mat-cell *matCellDef="let r">
              @if (editingId === r.id) {
                <mat-form-field appearance="outline" class="inline-edit wide">
                  <input matInput [(ngModel)]="editDescription" (keyup.enter)="saveRole(r)">
                </mat-form-field>
              } @else {
                {{ r.description || '—' }}
              }
            </td>
          </ng-container>

          <ng-container matColumnDef="roleType">
            <th mat-header-cell *matHeaderCellDef>Tipo</th>
            <td mat-cell *matCellDef="let r">
              <span class="role-badge">{{ r.roleType }}</span>
            </td>
          </ng-container>

          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef></th>
            <td mat-cell *matCellDef="let r">
              @if (editingId === r.id) {
                <button mat-icon-button color="primary" (click)="saveRole(r)" matTooltip="Guardar">
                  <mat-icon>check</mat-icon>
                </button>
                <button mat-icon-button (click)="cancelEdit()" matTooltip="Cancelar">
                  <mat-icon>close</mat-icon>
                </button>
              } @else {
                <button mat-icon-button (click)="startEdit(r)" [disabled]="r.isSystem" matTooltip="Editar">
                  <mat-icon>edit</mat-icon>
                </button>
                <button mat-icon-button color="warn" (click)="deleteRole(r)" [disabled]="r.isSystem" matTooltip="Eliminar">
                  <mat-icon>delete</mat-icon>
                </button>
              }
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="columns"></tr>
          <tr mat-row *matRowDef="let row; columns: columns;"></tr>
        </table>

        @if (roles().length === 0) {
          <div class="empty-state">
            <mat-icon>admin_panel_settings</mat-icon>
            <p>Sin roles registrados</p>
          </div>
        }
      </div>
    </div>
  `,
  styles: [`
    .page { max-width: 960px; }
    .page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
    .page-header h1 { font-size: 24px; font-weight: 700; margin-bottom: 4px; }
    .subtitle { color: var(--color-text-secondary); font-size: 14px; }
    .form-card { background: var(--color-surface); border-radius: var(--radius-md); padding: 20px; margin-bottom: 24px; border: 1px solid var(--color-border); }
    .form-card h3 { font-size: 16px; font-weight: 600; margin-bottom: 16px; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .full-width { grid-column: 1 / -1; }
    .form-actions { margin-top: 16px; display: flex; gap: 8px; }
    .table-container { background: var(--color-surface); border-radius: var(--radius-md); overflow: hidden; box-shadow: var(--shadow-sm); }
    .inline-edit { margin: -16px 0; width: 140px; }
    .inline-edit.wide { width: 260px; }
    .system-icon { font-size: 14px; width: 14px; height: 14px; color: var(--color-on-surface-muted); vertical-align: middle; margin-left: 4px; }
    .role-badge { padding: 2px 10px; border-radius: 12px; font-size: 12px; font-weight: 600; background: var(--color-surface-variant); color: var(--color-on-surface-variant); }
    .empty-state { text-align: center; padding: 40px; color: var(--color-text-secondary); }
    .empty-state mat-icon { font-size: 40px; width: 40px; height: 40px; margin-bottom: 8px; }
  `]
})
export class RoleManagementComponent implements OnInit {
  private admin = inject(AdminService);
  private snackBar = inject(MatSnackBar);

  roles = signal<RoleSummary[]>([]);
  columns = ['name', 'description', 'roleType', 'actions'];
  showCreateForm = false;
  editingId: string | null = null;
  editName = '';
  editDescription = '';

  createForm = { name: '', description: '', permissions: '[]', roleType: 'secondary' };

  ngOnInit() {
    this.loadRoles();
  }

  private loadRoles() {
    this.admin.getRoles().subscribe({
      next: (data) => this.roles.set(data),
      error: (err) => { console.error('Error loading roles', err); this.snackBar.open('Error al cargar roles', 'Cerrar', { duration: 4000 }); }
    });
  }

  createRole() {
    this.admin.createRole(this.createForm).subscribe({
      next: () => {
        this.snackBar.open('Rol creado exitosamente', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.showCreateForm = false;
        this.createForm = { name: '', description: '', permissions: '[]', roleType: 'secondary' };
        this.loadRoles();
      },
      error: (err) => this.snackBar.open(err.error?.message || 'Error al crear rol', 'Cerrar', { duration: 5000 })
    });
  }

  startEdit(r: RoleSummary) {
    this.editingId = r.id;
    this.editName = r.name;
    this.editDescription = r.description || '';
  }

  cancelEdit() {
    this.editingId = null;
  }

  saveRole(r: RoleSummary) {
    this.admin.updateRole(r.id, { name: this.editName, description: this.editDescription, permissions: r.permissions, roleType: r.roleType }).subscribe({
      next: () => {
        this.snackBar.open('Rol actualizado', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.editingId = null;
        this.loadRoles();
      },
      error: (err) => this.snackBar.open(err.error?.message || 'Error al actualizar rol', 'Cerrar', { duration: 5000 })
    });
  }

  deleteRole(r: RoleSummary) {
    if (r.isSystem) { this.snackBar.open('No se puede eliminar un rol del sistema', 'Cerrar', { duration: 4000 }); return; }
    if (!confirm(`¿Eliminar el rol "${r.name}"?`)) return;
    this.admin.deleteRole(r.id).subscribe({
      next: () => {
        this.snackBar.open('Rol eliminado', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.loadRoles();
      },
      error: (err) => this.snackBar.open(err.error?.message || 'Error al eliminar rol', 'Cerrar', { duration: 5000 })
    });
  }
}
