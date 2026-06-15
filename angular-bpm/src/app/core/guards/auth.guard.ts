import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const authGuard = () => {
  const router = inject(Router);
  const token = localStorage.getItem('bpm_token');
  if (!token) {
    return router.parseUrl('/auth/login');
  }
  return true;
};
