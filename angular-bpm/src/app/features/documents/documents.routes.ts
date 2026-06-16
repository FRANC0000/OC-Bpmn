import { Routes } from '@angular/router';

export default [
  { path: '', loadComponent: () => import('./pages/document-list.component').then(m => m.DocumentListComponent) },
  { path: 'new', loadComponent: () => import('./pages/document-form.component').then(m => m.DocumentFormComponent) },
  { path: ':id', loadComponent: () => import('./pages/document-form.component').then(m => m.DocumentFormComponent) },
] as Routes;
