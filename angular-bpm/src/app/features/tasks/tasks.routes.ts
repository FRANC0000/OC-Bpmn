import { Routes } from '@angular/router';

export default [
  {
    path: '',
    loadComponent: () => import('./pages/task-list.component').then(m => m.TaskListComponent)
  }
] as Routes;
