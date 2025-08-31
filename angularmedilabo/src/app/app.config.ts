/*import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { AuthInterceptor } from './auth.interceptor';


export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideClientHydration(), provideHttpClient(),provideZoneChangeDetection({ eventCoalescing: true })]
};*/

/*import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideClientHydration } from '@angular/platform-browser';
import { provideZoneChangeDetection } from '@angular/core';

import { routes } from './app.routes';
import { authInterceptor } from './auth.interceptor'; // nouvelle fonction

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    provideZoneChangeDetection({ eventCoalescing: true }),

    provideHttpClient(
      withInterceptors([
        authInterceptor
      ])
    )
  ]
};*/

import { ApplicationConfig, APP_INITIALIZER, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideClientHydration } from '@angular/platform-browser';
import { provideZoneChangeDetection } from '@angular/core';

import { routes } from './app.routes';
import { authInterceptor } from './auth.interceptor';
import { AuthService } from './services/auth.service';
import { HttpClientModule } from '@angular/common/http'; // nécessaire pour injection manuelle

/**
 * Fonction d'initialisation qui appelle initAnonymousSession()
 * Angular attend qu'elle soit résolue avant de lancer l'app.
 */
export function initAuthFactory(authService: AuthService): () => Promise<void> {
  return () => authService.initAnonymousSession();
}

export const appConfig: ApplicationConfig = {
  providers: [
    //  Routes
    provideRouter(routes),

    //  Hydratation du DOM (utile si SSR dans le futur)
    provideClientHydration(),

    //  Optimisation du détections de changements
    provideZoneChangeDetection({ eventCoalescing: true }),

    //  Http client avec notre interceptor pour ajouter le token
    provideHttpClient(withInterceptors([authInterceptor])),

    //  Fournir HttpClientModule explicitement (utile pour l'injection de AuthService)
    importProvidersFrom(HttpClientModule),

    //  Initialisation du token anonyme
    {
      provide: APP_INITIALIZER,
      useFactory: initAuthFactory,
      deps: [AuthService],
      multi: true,
    }
  ]
};