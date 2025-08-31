/*import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }
}*/
///////////////////////////////////

/*import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private anonymousTokenUrl = '/api/auth/anonymous'; // URL backend

  constructor(private http: HttpClient) {}
*/
  /**
   * Appelle le backend pour obtenir un token JWT anonyme
   */
  /*fetchAnonymousToken(): Observable<{ token: string }> {
    return this.http.get<{ token: string }>(this.anonymousTokenUrl);
  }
*/
  /**
   * Sauvegarde le token dans le localStorage
   * @param token JWT à sauvegarder
   *//*
  saveToken(token: string): void {
    if (this.isBrowser()) {
      localStorage.setItem('jwtToken', token);
    }
  }*/

  /**
   * Récupère le token depuis le localStorage
   *//*
  getToken(): string | null {
    if (this.isBrowser()) {
      return localStorage.getItem('jwtToken');
    }
    return null;
  }*/

  /**
   * Supprime le token du localStorage (ex: lors d’un logout)
   *//*
  clearToken(): void {
    if (this.isBrowser()) {
      localStorage.removeItem('jwtToken');
    }
  }*/

  /**
   * Vérifie si un token est déjà présent
   *//*
  isAuthenticated(): boolean {
    return !!this.getToken();
  }*/

  /**
   * Initialise la session anonyme si aucun token n’est présent
   */
  /*initAnonymousSession(): void {
    if (!this.isAuthenticated()) {
      this.fetchAnonymousToken().subscribe({
        next: (response) => this.saveToken(response.token),
        error: (err) => console.error('Erreur récupération token anonyme', err)
      });
    }
  }*/
 /**
   * On retourne une Promise pour APP_INITIALIZER
   *//*
  initAnonymousSession(): Promise<void> {
    if (this.isAuthenticated()) {
      return Promise.resolve();
    }

    return lastValueFrom(this.fetchAnonymousToken())
      .then(response => {
        this.saveToken(response.token);
      })
      .catch(err => {
        console.error('Erreur récupération token anonyme', err);
        // On résout quand même pour ne bloquer l'init de l'app
        return;
      });
  }*/

  /**
   * Vérifie si on est dans un navigateur (pas dans Node, ni lors de tests)
   *//*
  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }
}*/
/*import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, lastValueFrom, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private anonymousTokenUrl = '/api/auth/anonymous';
  private tokenSubject = new BehaviorSubject<string | null>(null);
  public token$ = this.tokenSubject.asObservable();

  constructor(private http: HttpClient) {
    const existingToken = this.getToken();
    if (existingToken) {
      this.tokenSubject.next(existingToken);
    }
  }

  fetchAnonymousToken(): Observable<{ token: string }> {
    return this.http.get<{ token: string }>(this.anonymousTokenUrl);
  }

  saveToken(token: string): void {
    if (this.isBrowser()) {
      localStorage.setItem('jwtToken', token);
      this.tokenSubject.next(token);
    }
  }

  getToken(): string | null {
    if (this.isBrowser()) {
      return localStorage.getItem('jwtToken');
    }
    return null;
  }

  clearToken(): void {
    if (this.isBrowser()) {
      localStorage.removeItem('jwtToken');
      this.tokenSubject.next(null);
    }
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  initAnonymousSession(): Promise<void> {
    if (this.isAuthenticated()) {
      this.tokenSubject.next(this.getToken());
      return Promise.resolve();
    }

    return lastValueFrom(this.fetchAnonymousToken())
      .then(response => {
        this.saveToken(response.token);
      })
      .catch(err => {
        console.error('Erreur récupération token anonyme', err);
        return;
      });
  }

  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }
}*/

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, lastValueFrom, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private anonymousTokenUrl = '/api/auth/anonymous';
  private tokenSubject = new BehaviorSubject<string | null>(null);
  public token$ = this.tokenSubject.asObservable();

  constructor(private http: HttpClient) {
    const storedToken = this.getToken(); // récupéré depuis localStorage
    if (storedToken) {
      this.tokenSubject.next(storedToken); // propager à token$
    }
  }

  fetchAnonymousToken(): Observable<{ token: string }> {
    return this.http.get<{ token: string }>(this.anonymousTokenUrl);
  }

  getToken(): string | null {
    if ((typeof window !== 'undefined') && (this.isBrowser())) {
      return localStorage.getItem('jwtToken')
    }
    return null;
    //return typeof window !== 'undefined' ? localStorage.getItem('jwtToken') : null;
  }

  saveToken(token: string): void {
    if ((typeof window !== 'undefined') && (this.isBrowser())) { //if (typeof window !== 'undefined') {
      localStorage.setItem('jwtToken', token);
      this.tokenSubject.next(token); // propager à l'observable
    }
  }

  initAnonymousSession(): Promise<void> {
    if (this.isAuthenticated()) {
      return Promise.resolve();
    }

    return lastValueFrom(this.fetchAnonymousToken())
      .then(response => {
        this.saveToken(response.token); // tokenSubject.next() sera appelé ici
      })
      .catch(err => {
        console.error('Erreur récupération token anonyme', err);
      });
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }

}
