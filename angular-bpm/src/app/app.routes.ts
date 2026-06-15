import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { MainLayoutComponent } from './core/layout/main-layout.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/tasks',
    pathMatch: 'full'
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes')
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'tasks', loadChildren: () => import('./features/tasks/tasks.routes') },
      { path: 'processes', loadChildren: () => import('./features/processes/processes.routes') },
      { path: 'designer', loadChildren: () => import('./features/designer/designer.routes') },
      { path: 'documents', loadChildren: () => import('./features/documents/documents.routes') },
      { path: 'admin', loadChildren: () => import('./features/admin/admin.routes') },
    ]
  },
  {
    path: '**',
    redirectTo: '/tasks'
  }
];
