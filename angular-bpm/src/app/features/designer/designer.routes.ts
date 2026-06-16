import { Routes } from '@angular/router';

export default [
  {
    path: '',
    loadComponent: () => import('./components/designer.component').then(m => m.DesignerComponent)
  },
  {
    path: ':processId',
    loadComponent: () => import('./components/designer.component').then(m => m.DesignerComponent)
  }
] as Routes;
