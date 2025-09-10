import { ApplicationConfig, APP_INITIALIZER, importProvidersFrom, inject } from '@angular/core';
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
export function appInitializer() {
  const authService = inject(AuthService);
  return () => authService.initAnonymousSession(); // doit retourner une Promise<void>
}

export const appConfig: ApplicationConfig = {
  providers: [
    //  Routes
    provideRouter(routes),

    //  Hydratation du DOM (utile si SSR dans le futur)
    provideClientHydration(),

    //  Optimisation du détections de changements
    provideZoneChangeDetection({ eventCoalescing: true }),

    //  Http client avec l'interceptor pour ajouter le token
    provideHttpClient(withInterceptors([authInterceptor])),

    //  Fournir HttpClientModule explicitement (utile pour l'injection de AuthService)
    importProvidersFrom(HttpClientModule),

    //  Initialisation du token anonyme
   {
      provide: APP_INITIALIZER,
      useFactory: appInitializer,
      multi: true
    }
  ]
};
