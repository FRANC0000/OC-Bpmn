import { Component, OnInit, inject, signal } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../core/services/admin.service';
import { RoleSummary } from '../../../core/models/admin.model';

@Component({
  selector: 'bpm-permission-management',
  standalone: true,
  imports: [
    MatIcon, MatTableModule, MatButtonModule, MatFormFieldModule,
    MatInputModule, MatSnackBarModule, MatTooltipModule, FormsModule
  ],
  template: `
    <div class="page">
      <div class="page-header">
        <div>
          <h1>Permisos</h1>
          <p class="subtitle">Gestiona los permisos asociados a cada rol</p>
        </div>
      </div>

      <div class="table-container">
        <table mat-table [dataSource]="roles()" class="data-table">
          <ng-container matColumnDef="name">
            <th mat-header-cell *matHeaderCellDef>Rol</th>
            <td mat-cell *matCellDef="let r">
              {{ r.name }}
              @if (r.isSystem) { <mat-icon class="system-icon" matTooltip="Rol del sistema">lock</mat-icon> }
            </td>
          </ng-container>

          <ng-container matColumnDef="roleType">
            <th mat-header-cell *matHeaderCellDef>Tipo</th>
            <td mat-cell *matCellDef="let r">
              <span class="role-badge">{{ r.roleType }}</span>
            </td>
          </ng-container>

          <ng-container matColumnDef="permissions">
            <th mat-header-cell *matHeaderCellDef>Permisos (JSON)</th>
            <td mat-cell *matCellDef="let r">
              @if (editingId === r.id) {
                <mat-form-field appearance="outline" class="perm-editor">
                  <textarea matInput [(ngModel)]="editPermissions" rows="4" placeholder='["read:users","write:users"]'></textarea>
                </mat-form-field>
              } @else {
                <code class="perm-preview">{{ r.permissions }}</code>
              }
            </td>
          </ng-container>

          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef></th>
            <td mat-cell *matCellDef="let r">
              @if (editingId === r.id) {
                <button mat-icon-button color="primary" (click)="savePermissions(r)" matTooltip="Guardar">
                  <mat-icon>check</mat-icon>
                </button>
                <button mat-icon-button (click)="cancelEdit()" matTooltip="Cancelar">
                  <mat-icon>close</mat-icon>
                </button>
              } @else {
                <button mat-icon-button (click)="startEdit(r)" matTooltip="Editar permisos">
                  <mat-icon>edit</mat-icon>
                </button>
              }
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="columns"></tr>
          <tr mat-row *matRowDef="let row; columns: columns;"></tr>
        </table>

        @if (roles().length === 0) {
          <div class="empty-state">
            <mat-icon>lock</mat-icon>
            <p>Sin roles registrados</p>
          </div>
        }
      </div>
    </div>
  `,
  styles: [`
    .page { max-width: 960px; }
    .page-header { margin-bottom: 24px; }
    .page-header h1 { font-size: 24px; font-weight: 700; margin-bottom: 4px; }
    .subtitle { color: var(--color-text-secondary); font-size: 14px; }
    .table-container { background: var(--color-surface); border-radius: var(--radius-md); overflow: hidden; box-shadow: var(--shadow-sm); }
    .perm-editor { margin: -12px 0; width: 100%; min-width: 300px; }
    .perm-preview { font-size: 12px; color: var(--color-on-surface-muted); word-break: break-all; }
    .system-icon { font-size: 14px; width: 14px; height: 14px; color: var(--color-on-surface-muted); vertical-align: middle; margin-left: 4px; }
    .role-badge { padding: 2px 10px; border-radius: 12px; font-size: 12px; font-weight: 600; background: var(--color-surface-variant); color: var(--color-on-surface-variant); }
    .empty-state { text-align: center; padding: 40px; color: var(--color-text-secondary); }
    .empty-state mat-icon { font-size: 40px; width: 40px; height: 40px; margin-bottom: 8px; }
  `]
})
export class PermissionManagementComponent implements OnInit {
  private admin = inject(AdminService);
  private snackBar = inject(MatSnackBar);

  roles = signal<RoleSummary[]>([]);
  columns = ['name', 'roleType', 'permissions', 'actions'];
  editingId: string | null = null;
  editPermissions = '';

  ngOnInit() {
    this.loadRoles();
  }

  private loadRoles() {
    this.admin.getRoles().subscribe({
      next: (data) => this.roles.set(data),
      error: (err) => { console.error('Error loading roles', err); this.snackBar.open('Error al cargar roles', 'Cerrar', { duration: 4000 }); }
    });
  }

  startEdit(r: RoleSummary) {
    this.editingId = r.id;
    this.editPermissions = r.permissions;
  }

  cancelEdit() {
    this.editingId = null;
  }

  savePermissions(r: RoleSummary) {
    try { JSON.parse(this.editPermissions); } catch {
      this.snackBar.open('JSON de permisos inválido', 'Cerrar', { duration: 4000 });
      return;
    }
    this.admin.updateRole(r.id, { name: r.name, permissions: this.editPermissions, roleType: r.roleType }).subscribe({
      next: () => {
        this.snackBar.open('Permisos actualizados', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.editingId = null;
        this.loadRoles();
      },
      error: (err) => this.snackBar.open(err.error?.message || 'Error al actualizar permisos', 'Cerrar', { duration: 5000 })
    });
  }
}
