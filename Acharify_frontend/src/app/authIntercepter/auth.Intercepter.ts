import { Router } from '@angular/router';
import { inject } from '@angular/core';
import { tap } from 'rxjs';
import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = typeof window !== 'undefined' ? localStorage.getItem('accessToken') : null;

  if (token) {
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    return next(clonedReq).pipe(
      tap({
        error: (error) => {
          if (error.status === 401) {
            // Unauthorized, redirect to login
            localStorage.clear();
            router.navigate(['/login']);
          }
        }
      })
    );
  }

  return next(req);
};
