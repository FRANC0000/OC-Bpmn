import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth.guard';

export default [
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/document-list.component').then(m => m.DocumentListComponent)
  }
] as Routes;
