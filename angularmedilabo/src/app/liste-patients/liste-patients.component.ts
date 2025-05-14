import { Component,OnInit } from '@angular/core';
import { DetailsPatientComponent } from '../details-patient/details-patient.component';
import { Router } from '@angular/router';
//import { HttpClient } from '@angular/common/http';
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
  //constructor(private router: Router, private patientsService: PatientsService) { }
  
patientForm!: FormGroup;
  
  constructor(private router: Router,private formBuilder: FormBuilder, private patientsService: PatientsService) { 
    this.patients$ = this.patientsService.patients$;
    //this.patients$ = this.patientsService.getAllPatients();
    /*this.patientForm = this.formBuilder.group({
      id: [null],
      nom: [null,[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
      prenom: [null,[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
      dateNaissance: [null,[Validators.required,[Validators.pattern('^(19\d{2}|20\d{2})$)-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$')]]],
      
    });*/
  } 
  //patients!: DetailsPatientComponent[];

  ngOnInit() {
    // Appel de la méthode du service
    this.patients$ = this.patientsService.getAllPatients();
    /*this.patientsService.getAllPatients().subscribe(data => {
      this.patients = data;
    });*/
    /*this.patientForm = this.formBuilder.group({
              id: [null],
              nom: [null,[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
              prenom: [null,[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
              dateNaissance: [null,[Validators.required,[Validators.pattern('^(19\d{2}|20\d{2})$)-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$')]],
              genre: [null,[Validators.required]],
              adresse: [null],
              telephone: [null, [Validators.pattern('^\d{3}-\d{3}-\d{4}$')]]
          });*/
  }
  onCreatePatient() {
    this.router.navigate(['/details-patient']);//this.router.navigateByUrl('/crea-patient');
  }
  
  onModify(patientId: number): void {
    this.router.navigate(['/details-patient',patientId]);
  }

  onOpenHistory() {
    this.router.navigateByUrl('/list-history-patient');
  }
}
