/**/
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './services/auth.service';
import { filter, switchMap, take } from 'rxjs/operators';
import { of } from 'rxjs';
//import { take, switchMap, of } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  /**/
  if (req.url.includes('/api/auth/anonymous')) {
  return next(req);
}
  return authService.token$.pipe(
    filter(token => !!token), // ne passe que si token est non-null / non-undefined
    take(1), // attendre la première valeur non-null
    switchMap(token => {
      if (token) {
        console.log('Intercepteur - Token utilisé :', token);
        const cloned = req.clone({
          headers: req.headers.set('Authorization', `Bearer ${token}`)
        });
        return next(cloned);
      } else {
        // Aucun token disponible, on envoie la requête sans Authorization
        return next(req);
      }
    })
  );
};/**/
