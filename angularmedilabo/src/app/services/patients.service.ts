import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Patient } from '../models/patient';

@Injectable({
  providedIn: 'root'
})
export class PatientsService {
  private patientsUpdated = new BehaviorSubject<void>(undefined);
  private patientsSubject: BehaviorSubject<Patient[]>;

  // Observable public pour la consommation
  public patients$: Observable<Patient[]>;
  //private apiUrl = '/patients';
  //private apiUrl = 'http://micropatient:8999/patients';
  private apiUrl = '/api/patients';

  constructor(private http: HttpClient) { 
    // Initialisation avec un tableau vide ou des données initiales
    const initialPatients: Patient[] = [];
    this.patientsSubject = new BehaviorSubject<Patient[]>(initialPatients);
    this.patients$ = this.patientsSubject.asObservable();
  }

  getAllPatients(): Observable<Patient[]> {
    //return this.http.get<Patient[]>('http://localhost:8999/patients');
    //return this.http.get<Patient[]>('http://micropatient:8999/patients');
    return this.http.get<Patient[]>('/api/patients');
  }

  getPatientById(id: number): Observable<Patient> {
    //return this.http.get<Patient>(`${'http://localhost:8999/patients'}/${id}`);
    //return this.http.get<Patient>(`${'http://micropatient:8999/patients'}/${id}`);
    return this.http.get<Patient>(`${'/api/patients'}/${id}`);
  }

  addPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(this.apiUrl, patient);
  }

  updatePatient(patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(`${this.apiUrl}/${patient.id}`, patient);
  }
  
  getPatientRisque(patientId: number): Observable<string> {
    /*return this.http.get(`${'http://localhost:8997/evaluer'}/${patientId}`, {
    responseType: 'text'
  });*/
    /*return this.http.get(`${'http://microrisque:8997/evaluer'}/${patientId}`, {
    responseType: 'text'
  });*/
  return this.http.get(`${'/api/evaluer'}/${patientId}`, {
    responseType: 'text'
  });
  }

}
