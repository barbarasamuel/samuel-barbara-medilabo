import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Patient } from '../models/patient';

@Injectable({
  providedIn: 'root'
})
export class PatientsService {
  //private tableau: Patient[] = [];
  private patientsSubject: BehaviorSubject<Patient[]>;

  // Observable public pour la consommation
  public patients$: Observable<Patient[]>;
  private apiUrl = 'http://localhost:8999/Patients';

  constructor(private http: HttpClient) { 
    // Initialisation avec un tableau vide ou des données initiales
    const initialPatients: Patient[] = [];
    this.patientsSubject = new BehaviorSubject<Patient[]>(initialPatients);
    this.patients$ = this.patientsSubject.asObservable();
  }

  getAllPatients(): Observable<Patient[]> {
    return this.http.get<Patient[]>('http://localhost:8999/Patients');
  
  /*this.http.get<Patient[]>('http://localhost:8999/Patients')
  .pipe(
    tap(patients => this.patientsSubject.next(patients))
  )
  .subscribe();*/
  }

  getPatientById(id: number): Observable<Patient> {
    return this.http.get<Patient>(`${'http://localhost:8999/Patients'}/${id}`);
  }

  addPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(this.apiUrl, patient);
  }

  updatePatient(patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(`${this.apiUrl}/${patient.id}`, patient);
  }
  // Méthode pour ajouter un patient
  /*addPatient(patient: Patient): void {
    const currentPatients = this.patientsSubject.value;
    this.patientsSubject.next([...currentPatients, patient]);
  }*/
  /*addPatient(newPatient: Patient): void {

    // Ajout côté serveur
    this.http.post<Patient>('http://localhost:8999/Patients', newPatient).pipe(
      tap(() => this.loadPatients()) // recharge la liste après ajout
    ).subscribe();
  }

  // Méthode pour mettre à jour un patient
  updatePatient(updatedPatient: Patient): void {
    const url = `${'http://localhost:8999/Patients'}/${updatedPatient.id}`;
    this.http.patch<Patient>(url, updatedPatient).subscribe({
      next: (patient) => {
        const patients = this.patientsSubject.value.map(p =>
          p.id === patient.id ? patient : p
        );
        this.patientsSubject.next(patients);
      },
      error: (err) => console.error('Erreur lors de la modification du patient', err)
    });
  }*/
}
