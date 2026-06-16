import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../core/services/admin.service';
import { TenantSummary, AdminUserSummary } from '../../../core/models/admin.model';

@Component({
  selector: 'bpm-user-management',
  standalone: true,
  imports: [
    MatIcon, MatTableModule, MatButtonModule, MatFormFieldModule,
    MatInputModule, MatSelectModule, MatCardModule, FormsModule, MatTooltipModule
  ],
  template: `
    <div class="page">
      <div class="page-header">
        <div>
          <h1>Usuarios</h1>
          <p class="subtitle">Gestiona los usuarios de cada inquilino</p>
        </div>
      </div>

      <div class="controls">
        <mat-form-field appearance="outline" class="tenant-select">
          <mat-label>Seleccionar inquilino</mat-label>
          <mat-select [(ngModel)]="selectedTenantId" (selectionChange)="onTenantChange()">
            @for (t of tenants(); track t.id) {
              <mat-option [value]="t.id">{{ t.name }} ({{ t.slug }})</mat-option>
            }
          </mat-select>
        </mat-form-field>

        <button mat-flat-button color="primary" [disabled]="!selectedTenantId" (click)="showForm = !showForm">
          <mat-icon>add</mat-icon>
          {{ showForm ? 'Cancelar' : 'Nuevo usuario' }}
        </button>
      </div>

      @if (showForm && selectedTenantId) {
        <mat-card class="form-card">
          <mat-card-content>
            <h3>Crear usuario en {{ selectedTenantName() }}</h3>
            <div class="form-grid">
              <mat-form-field appearance="outline">
                <mat-label>Nombre completo</mat-label>
                <input matInput [(ngModel)]="newUser.displayName" placeholder="Nombre">
              </mat-form-field>
              <mat-form-field appearance="outline">
                <mat-label>Email</mat-label>
                <input matInput type="email" [(ngModel)]="newUser.email" placeholder="email@ejemplo.com">
              </mat-form-field>
              <mat-form-field appearance="outline">
                <mat-label>Contraseña</mat-label>
                <input matInput type="password" [(ngModel)]="newUser.password" placeholder="••••••">
              </mat-form-field>
            </div>
            <div class="form-actions">
              <button mat-flat-button color="primary" (click)="createUser()" [disabled]="!newUser.displayName || !newUser.email || !newUser.password">
                <mat-icon>person_add</mat-icon>
                Crear usuario
              </button>
            </div>
          </mat-card-content>
        </mat-card>
      }

      @if (selectedTenantId) {
        <div class="table-container">
          <table mat-table [dataSource]="users()" class="data-table">
            <ng-container matColumnDef="displayName">
              <th mat-header-cell *matHeaderCellDef>Nombre</th>
              <td mat-cell *matCellDef="let u">
                @if (editingUserId === u.id) {
                  <mat-form-field appearance="outline" class="inline-edit">
                    <input matInput [(ngModel)]="editDisplayName" (keyup.enter)="saveUser(u)">
                  </mat-form-field>
                } @else {
                  {{ u.displayName }}
                }
              </td>
            </ng-container>

            <ng-container matColumnDef="email">
              <th mat-header-cell *matHeaderCellDef>Email</th>
              <td mat-cell *matCellDef="let u">{{ u.email }}</td>
            </ng-container>

            <ng-container matColumnDef="role">
              <th mat-header-cell *matHeaderCellDef>Rol</th>
              <td mat-cell *matCellDef="let u">
                <mat-form-field appearance="outline" class="role-select" (click)="$event.stopPropagation()">
                  <mat-select [value]="u.role || 'USER'" (selectionChange)="changeRole(u, $event.value)">
                    <mat-option value="ADMIN">ADMIN</mat-option>
                    <mat-option value="USER">USER</mat-option>
                  </mat-select>
                </mat-form-field>
              </td>
            </ng-container>

            <ng-container matColumnDef="status">
              <th mat-header-cell *matHeaderCellDef>Estado</th>
              <td mat-cell *matCellDef="let u">
                <span class="status-badge" [class.active]="u.status === 'active'" [class.inactive]="u.status === 'inactive'">
                  {{ u.status }}
                </span>
              </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef></th>
              <td mat-cell *matCellDef="let u">
                @if (editingUserId === u.id) {
                  <button mat-icon-button color="primary" (click)="saveUser(u)" matTooltip="Guardar">
                    <mat-icon>check</mat-icon>
                  </button>
                  <button mat-icon-button (click)="cancelEdit()" matTooltip="Cancelar">
                    <mat-icon>close</mat-icon>
                  </button>
                } @else {
                  <button mat-icon-button (click)="startEdit(u)" matTooltip="Editar nombre">
                    <mat-icon>edit</mat-icon>
                  </button>
                  <button mat-icon-button (click)="toggleUserStatus(u)" [matTooltip]="u.status === 'active' ? 'Desactivar' : 'Activar'">
                    <mat-icon>{{ u.status === 'active' ? 'toggle_off' : 'toggle_on' }}</mat-icon>
                  </button>
                  <button mat-icon-button color="warn" (click)="deleteUser(u)" matTooltip="Eliminar">
                    <mat-icon>delete</mat-icon>
                  </button>
                }
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="userColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: userColumns;"></tr>
          </table>

          @if (users().length === 0) {
            <div class="empty-state">
              <mat-icon>people_outline</mat-icon>
              <p>Sin usuarios en este inquilino</p>
            </div>
          }
        </div>
      } @else {
        <div class="placeholder-state">
          <mat-icon class="placeholder-icon">people</mat-icon>
          <h3>Selecciona un inquilino</h3>
          <p>Elige un inquilino para ver y gestionar sus usuarios</p>
        </div>
      }
    </div>
  `,
  styles: [`
    .page { max-width: 960px; }
    .page-header { margin-bottom: 24px; }
    .page-header h1 { font-size: 24px; font-weight: 700; margin-bottom: 4px; }
    .subtitle { color: var(--color-text-secondary); font-size: 14px; }
    .controls { display: flex; gap: 16px; align-items: center; margin-bottom: 24px; flex-wrap: wrap; }
    .tenant-select { min-width: 320px; flex: 1; }
    .form-card { margin-bottom: 24px; }
    .form-card h3 { font-size: 16px; font-weight: 600; margin-bottom: 16px; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .form-actions { margin-top: 16px; display: flex; gap: 8px; }
    .table-container { background: var(--color-surface); border-radius: var(--radius-md); overflow: hidden; box-shadow: var(--shadow-sm); }
    .inline-edit { margin: -16px 0; width: 160px; }
    .role-select { margin: -16px 0; width: 110px; }
    ::ng-deep .role-select .mdc-notched-outline__notch { border-right: none; }
    .placeholder-state { text-align: center; padding: 80px 24px; background: var(--color-surface); border-radius: var(--radius-md); border: 1px dashed var(--color-border); }
    .placeholder-icon { font-size: 56px; width: 56px; height: 56px; color: var(--color-text-disabled); margin-bottom: 16px; }
    .placeholder-state h3 { font-size: 18px; font-weight: 600; margin-bottom: 8px; }
    .placeholder-state p { color: var(--color-text-secondary); font-size: 14px; }
    .empty-state { text-align: center; padding: 40px; color: var(--color-text-secondary); }
    .empty-state mat-icon { font-size: 40px; width: 40px; height: 40px; margin-bottom: 8px; }
  `]
})
export class UserManagementComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private admin = inject(AdminService);
  private snackBar = inject(MatSnackBar);

  tenants = signal<TenantSummary[]>([]);
  users = signal<AdminUserSummary[]>([]);
  selectedTenantId = '';
  showForm = false;
  editingUserId: string | null = null;
  editDisplayName = '';

  newUser = { displayName: '', email: '', password: '' };
  userColumns = ['displayName', 'email', 'role', 'status', 'actions'];

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (params['tenantId']) {
        this.selectedTenantId = params['tenantId'];
      }
    });
    this.loadTenants();
  }

  private loadTenants() {
    this.admin.getTenants().subscribe({
      next: (data) => {
        this.tenants.set(data);
        if (this.selectedTenantId && data.some(t => t.id === this.selectedTenantId)) {
          this.onTenantChange();
        }
      },
      error: (err) => { console.error('Error loading tenants', err); this.snackBar.open('Error al cargar inquilinos (' + (err.status || err.message) + ')', 'Cerrar', { duration: 6000 }); }
    });
  }

  selectedTenantName() {
    const t = this.tenants().find(x => x.id === this.selectedTenantId);
    return t ? t.name : '';
  }

  onTenantChange() {
    if (!this.selectedTenantId) return;
    this.showForm = false;
    this.admin.getTenantUsers(this.selectedTenantId).subscribe({
      next: (data) => { this.users.set(data); },
      error: (err) => { console.error('Error loading users', err); this.snackBar.open('Error al cargar usuarios (' + (err.status || err.message) + ')', 'Cerrar', { duration: 5000 }); }
    });
  }

  createUser() {
    if (!this.selectedTenantId) return;
    this.admin.createUser({
      email: this.newUser.email,
      password: this.newUser.password,
      displayName: this.newUser.displayName,
      tenantId: this.selectedTenantId
    }).subscribe({
      next: () => {
        this.snackBar.open('Usuario creado exitosamente', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.newUser = { displayName: '', email: '', password: '' };
        this.showForm = false;
        this.onTenantChange();
      },
      error: (err) => this.snackBar.open(err.error?.message || 'Error al crear usuario', 'Cerrar', { duration: 5000 })
    });
  }

  startEdit(u: AdminUserSummary) {
    this.editingUserId = u.id;
    this.editDisplayName = u.displayName;
  }

  cancelEdit() {
    this.editingUserId = null;
  }

  saveUser(u: AdminUserSummary) {
    this.admin.updateUser(u.id, u.tenantId, { displayName: this.editDisplayName, email: u.email }).subscribe({
      next: () => {
        this.snackBar.open('Usuario actualizado', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.editingUserId = null;
        this.onTenantChange();
      },
      error: (err) => this.snackBar.open(err.error?.message || 'Error al actualizar usuario', 'Cerrar', { duration: 5000 })
    });
  }

  changeRole(u: AdminUserSummary, newRole: string) {
    if (newRole === u.role) return;
    this.admin.updateUserRole(u.id, u.tenantId, { role: newRole }).subscribe({
      next: () => {
        this.snackBar.open('Rol actualizado', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.onTenantChange();
      },
      error: (err) => {
        const msg = err.error?.message || 'Error al cambiar rol';
        this.snackBar.open(msg, 'Cerrar', { duration: 6000 });
        if (msg.includes('last ADMIN')) {
          this.snackBar.open('No se puede cambiar el rol del último ADMIN del inquilino', 'Cerrar', { duration: 6000 });
        }
      }
    });
  }

  toggleUserStatus(u: AdminUserSummary) {
    const newStatus = u.status === 'active' ? 'inactive' : 'active';
    this.admin.updateUserStatus(u.id, u.tenantId, { status: newStatus }).subscribe({
      next: () => {
        this.snackBar.open(`Usuario ${newStatus === 'active' ? 'activado' : 'desactivado'}`, '', { duration: 3000, panelClass: 'success-snackbar' });
        this.onTenantChange();
      },
      error: (err) => {
        const msg = err.error?.message || 'Error al cambiar estado';
        this.snackBar.open(msg, 'Cerrar', { duration: 5000 });
        if (msg.includes('last ADMIN')) {
          this.snackBar.open('No se puede desactivar el último ADMIN del inquilino', 'Cerrar', { duration: 6000 });
        }
      }
    });
  }

  deleteUser(u: AdminUserSummary) {
    if (!confirm(`¿Eliminar al usuario "${u.displayName}" (${u.email})? Esta acción no se puede deshacer.`)) return;

    this.admin.deleteUser(u.id, u.tenantId).subscribe({
      next: () => {
        this.snackBar.open('Usuario eliminado', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.onTenantChange();
      },
      error: (err) => {
        const msg = err.error?.message || 'Error al eliminar usuario';
        this.snackBar.open(msg, 'Cerrar', { duration: 5000 });
        if (msg.includes('last ADMIN')) {
          this.snackBar.open('No se puede eliminar el último ADMIN del inquilino', 'Cerrar', { duration: 6000 });
        }
      }
    });
  }
}
