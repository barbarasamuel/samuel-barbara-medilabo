import { Component,OnInit } from '@angular/core';
//import { DetailsPatientComponent } from '../details-patient/details-patient.component';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { PatientsService } from '../services/patients.service';
import { Observable } from 'rxjs';
import { Patient } from '../models/patient';
import {CommonModule} from '@angular/common';


@Component({
  selector: 'app-liste-patients',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './liste-patients.component.html',
  styleUrl: './liste-patients.component.css'
})

export class ListePatientsComponent  implements OnInit {
  patients$!: Observable<Patient[]>;
  patients: any[] = [];
  
  patientForm!: FormGroup;
  
  constructor(private router: Router,private formBuilder: FormBuilder, private patientsService: PatientsService) { 
    this.patients$ = this.patientsService.patients$;
    
  } 

  ngOnInit() {
    // Appel de la méthode du service
    this.patients$ = this.patientsService.getAllPatients();

    //Rechargement de la liste
    this.loadPatients();
  }

  loadPatients() {
    this.patientsService.getAllPatients().subscribe(data => {
      this.patients = data;
    });
  }

  onCreatePatient() {
    this.router.navigate(['/details-patient']);
  }
  
  onModify(patientId: number): void {
    this.router.navigate(['/details-patient',patientId]);
  }

  onOpenHistory(patientId: number): void {
    this.router.navigate(['/liste-histo',patientId]);
  }

  /*formatDateJsonForDatetimeLocal(dateString: string): string {
    const date = new Date(dateString);
    const offset = date.getTimezoneOffset();
    const localDate = new Date(date.getTime() - offset * 60 * 1000);
    return localDate.toISOString().slice(0, 16);
  }*/
}
