import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { Histo } from '../models/histo';

@Injectable({
  providedIn: 'root'
})
export class HistoService {

  constructor(private http: HttpClient) { 
    // Initialisation avec un tableau vide ou des données initiales
    const initialHisto: Histo[] = [];
  }

  getAllHistoPatient(idPatient: number): Observable<Histo[]> {
    //return this.http.get<Histo[]>(`${'http://localhost:8998/hist'}/${idPatient}`);
    //return this.http.get<Histo[]>(`${'http://microhisto:8998/hist'}/${idPatient}`);
    return this.http.get<Histo[]>(`${'/api/hist'}/${idPatient}`);
  }

  getHistoById(id: string): Observable<Histo> {
    //return this.http.get<Histo>(`${'http://localhost:8998/hist/details'}/${id}`);
    //return this.http.get<Histo>(`${'http://microhisto:8998/hist/details'}/${id}`);
    return this.http.get<Histo>(`${'/api/hist/details'}/${id}`);
  }

  postAddHisto(histo: Histo): Observable<Histo> {
    //return this.http.post<Histo>('http://localhost:8998/hist/creation', histo);
    //return this.http.post<Histo>('http://microhisto:8998/hist/creation', histo); 
    return this.http.post<Histo>('/api/hist/creation', histo);
  }

}
