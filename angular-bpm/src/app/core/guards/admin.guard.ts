import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const adminGuard = () => {
  const router = inject(Router);
  const auth = inject(AuthService);
  if (!auth.isAdmin()) {
    return router.parseUrl('/tasks');
  }
  return true;
};
