/**/

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, lastValueFrom, Observable } from 'rxjs';
import { AuthResponse } from '../models/auth-response';

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

  fetchAnonymousToken(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(this.anonymousTokenUrl);
  }

  getToken(): string | null {

    if ((typeof window !== 'undefined') && (this.isBrowser())) {

      //////////////////////////////////
      lastValueFrom(this.fetchAnonymousToken())
        .then(response => {
          if (response?.accessToken) {
            this.saveToken(response);
          } else {
            console.error('Token manquant dans la réponse du serveur:', response);
          }
        })
        .catch(err => {
          console.error('Erreur récupération token anonyme', err);
        });
      //////////////////////////////////

        const token = localStorage.getItem('jwtToken');
        console.log('Token localStorage :', localStorage.getItem('jwtToken'));
        return token && token !== 'undefined' ? token : null;

    }
    return null;
  }

  saveToken(authresponse: AuthResponse | undefined | null): void {
    if (authresponse && this.isBrowser()) {
      console.log('Token enregistré :', authresponse);
      localStorage.setItem('jwtToken', authresponse.accessToken);
      localStorage.setItem('refreshToken', authresponse.refreshToken);
      localStorage.setItem('username', authresponse.username);
      localStorage.setItem('authorities', JSON.stringify(authresponse.authorities || []));
      this.tokenSubject.next(authresponse.accessToken);
    } else {
      console.warn('Token invalide non enregistré :', authresponse);
    }
  }

  initAnonymousSession(): Promise<void> {
    if (this.isAuthenticated()) {
      return Promise.resolve();
    }

    /**/
    return lastValueFrom(this.fetchAnonymousToken())
      .then(response => {
        if (response?.accessToken) {//if (response?.token) {
          this.saveToken(response); //this.saveToken(response.accessToken); //this.saveToken(response.token);
        } else {
          console.error('Token manquant dans la réponse du serveur:', response);
        }
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
