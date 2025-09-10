import { Component,OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { PatientsService } from '../services/patients.service';
import { Observable } from 'rxjs';
import { Patient } from '../models/patient';
import { AuthService } from '../services/auth.service';
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

  constructor(private authService: AuthService, private route: ActivatedRoute, private router: Router,private formBuilder: FormBuilder, private patientsService: PatientsService) {
    this.patients$ = this.patientsService.patients$;

  }

  ngOnInit() {

    // Appel de la méthode du service
    this.patients$ = this.patientsService.getAllPatients();

    //Rechargement de la liste
    this.loadPatients();

  }

  loadPatients() {
    this.patients = this.route.snapshot.data['patients'];
    console.log('rafraichissement');

  }

  onCreatePatient() {
    this.router.navigate(['/details-patient']);

  }

  onModify(patientId: number): void {
    this.router.navigate(['/details-patient',patientId]);

  }

  onOpenHistory(patientId: number, patient: string): void {
    this.router.navigate(['/liste-histo',patientId, patient]);
  }

}
