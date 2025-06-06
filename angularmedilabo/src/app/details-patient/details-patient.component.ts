import { Component, Input, OnInit, Pipe, PipeTransform } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormBuilder,FormGroup,Validators,ReactiveFormsModule } from '@angular/forms';
import { PatientsService } from '../services/patients.service';
import { ActivatedRoute,Router } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Patient } from '../models/patient';
import { CommonModule,JsonPipe,NgIf, DatePipe } from '@angular/common'; 

@Component({
  selector: 'app-details-patient',
  standalone: true,
  imports: [RouterOutlet,ReactiveFormsModule,NgIf,JsonPipe, DatePipe],
  templateUrl: './details-patient.component.html',
  styleUrl: './details-patient.component.css'
})
export class DetailsPatientComponent implements OnInit{
  patients$!: Observable<Patient>;
  
  patientForm!: FormGroup;
  @Input() mode: 'create' | 'edit' | 'view' = 'view';
  @Input() patientId?: number;
  @Input() patient!: Patient;
  isReadOnly = false;
    
  constructor(private router: Router, private formBuilder: FormBuilder,private route: ActivatedRoute,
    private patientsService: PatientsService) { 
      
  }
  
  ngOnInit(): void {

    const modeFromRoute = this.route.snapshot.data['mode'];
    if (modeFromRoute) {
      this.mode = modeFromRoute;
    }

    this.patientForm = this.formBuilder.group({
      nom: ['',[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
      prenom: ['',[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
      //dateNaissance: ['',[Validators.pattern('^(19|20)\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$')]],
      dateNaissance: ['',[Validators.required]],
      genre: ['',[Validators.required]],
      adresse: [''],
      telephone: ['', [Validators.pattern('^\d{3}-\d{3}-\d{4}$')]]
    }, {
      updateOn: 'blur'
    });

    if (this.mode==="edit") { 
      const patientId = Number(this.route.snapshot.paramMap.get('id'));
      this.patientsService.getPatientById(patientId).subscribe(data =>{
        this.patient = data;
        this.initializeForm();
      });
    }
          
    this.isReadOnly = this.mode === 'view';

    if (this.mode === 'view') {
      this.patientForm.disable();
    }
    
  }

  initializeForm(){

    this.patientForm = this.formBuilder.group({
      id:[this.patient.id],
      nom: [this.patient.nom],
      prenom: [this.patient.prenom],
      dateNaissance: [this.formatDateJsonForDatetimeLocal(this.patient.dateNaissance)],
      genre: [this.patient.genre],
      adresse: [this.patient.adressePostale],
      telephone: [this.patient.numTel]
    }, {
      updateOn: 'blur'
    });
  }

  
  onSave(){
    
    //console.log(this.patientForm.get('genre')?.errors);
    if (this.patientForm.invalid) return;

    const patientData = this.patientForm.value;
    this.patient = patientData;
  
    if (this.mode === 'create') {
      this.patientsService.addPatient(this.patient).subscribe((response) => {
      //this.patientsService.addPatient(patientData).subscribe((response) => {
                console.log('Nouveau patient:', response);
            });
    } else if (this.mode === 'edit') {

      this.patientsService.updatePatient(this.patient).subscribe((response) => {
                console.log('Patient mis à jour:', response);
        });
    }

    this.router.navigate(['/liste-patients']);
  }
  
  formatDateJsonForDatetimeLocal(dateString: string): string {
    const date = new Date(dateString);
    const offset = date.getTimezoneOffset();
    const localDate = new Date(date.getTime() - offset * 60 * 1000);
    return localDate.toISOString().slice(0, 10);
  }
}
