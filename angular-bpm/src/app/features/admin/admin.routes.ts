import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth.guard';

export default [
  {
    path: 'users',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/user-management.component').then(m => m.UserManagementComponent)
  },
  {
    path: 'tenants',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/tenant-config.component').then(m => m.TenantConfigComponent)
  },
  { path: '', redirectTo: 'users', pathMatch: 'full' }
] as Routes;
