import { Routes } from '@angular/router';

export default [
  {
    path: '',
    loadComponent: () => import('./pages/process-list.component').then(m => m.ProcessListComponent)
  }
] as Routes;
