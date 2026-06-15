import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth.guard';

export default [
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./components/designer.component').then(m => m.DesignerComponent)
  }
] as Routes;
