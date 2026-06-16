import { Routes } from '@angular/router';
import { adminGuard } from '../../core/guards/admin.guard';

export default [
  {
    path: '',
    canActivate: [adminGuard],
    children: [
      {
        path: 'users',
        loadComponent: () => import('./pages/user-management.component').then(m => m.UserManagementComponent)
      },
      {
        path: 'tenants',
        loadComponent: () => import('./pages/tenant-config.component').then(m => m.TenantConfigComponent)
      },
      {
        path: 'roles',
        loadComponent: () => import('./pages/role-management.component').then(m => m.RoleManagementComponent)
      },
      {
        path: 'permissions',
        loadComponent: () => import('./pages/permission-management.component').then(m => m.PermissionManagementComponent)
      },
      { path: '', redirectTo: 'users', pathMatch: 'full' }
    ]
  }
] as Routes;
