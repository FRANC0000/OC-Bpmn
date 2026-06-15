import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth.guard';

export default [
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/task-list.component').then(m => m.TaskListComponent)
  }
] as Routes;
