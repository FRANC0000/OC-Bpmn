import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { tap } from 'rxjs/operators';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.getToken();
  const user = auth.currentUser();

  let headers = req.headers;
  if (token) {
    headers = headers.set('Authorization', `Bearer ${token}`);
  }
  const tenantId = user?.tenantId || localStorage.getItem('bpm_tenant_id');
  if (tenantId) {
    headers = headers.set('X-Tenant-Id', tenantId);
  }

  const cloned = headers !== req.headers ? req.clone({ headers }) : req;

  return next(cloned).pipe(
    tap({ error: (err) => {
      if (err instanceof HttpErrorResponse && err.status === 401) {
        auth.logout();
      }
    }})
  );
};
