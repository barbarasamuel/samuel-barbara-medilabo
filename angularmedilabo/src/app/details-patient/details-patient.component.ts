import { Component, Input, OnInit, Pipe, PipeTransform } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormBuilder,FormGroup,Validators,ReactiveFormsModule,ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { PatientsService } from '../services/patients.service';
import { ActivatedRoute,Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Patient } from '../models/patient';
import { JsonPipe,NgIf, DatePipe } from '@angular/common'; //CommonModule,

@Component({
  selector: 'app-details-patient',
  standalone: true,
  imports: [RouterOutlet,ReactiveFormsModule,NgIf,JsonPipe, DatePipe],
  templateUrl: './details-patient.component.html',
  styleUrl: './details-patient.component.css'
})
export class DetailsPatientComponent implements OnInit{
  patients$!: Observable<Patient>;
  note : string = '';

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
      dateNaissance: ['',[Validators.required]],
      genre: ['',[Validators.required]],
      adresse: [''],
      telephone: ['', telephoneValidator()]
    }, {
      updateOn: 'blur'
    });

    if (this.mode==="edit") {
      const patientId = Number(this.route.snapshot.paramMap.get('id'));
      this.patientsService.getPatientById(patientId).subscribe(data =>{
        this.patient = data;
        this.initializeForm();
      });

      this.patientsService.getPatientRisque(patientId).subscribe((data) => this.note = data);

    }

    function telephoneValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value?.trim();
      if (!value) return null; // champ facultatif

      const isValid = /^\d{3}-\d{3}-\d{4}$/.test(value);
      return isValid ? null : { patternInvalid: true };
    };
  }
  }

  initializeForm(){

    this.patientForm = this.formBuilder.group({
      id:[this.patient.id],
      nom: [this.patient.nom],
      prenom: [this.patient.prenom],
      dateNaissance: [this.formatDateJsonForDatetimeLocal(this.patient.dateNaissance)],
      genre: [this.patient.genre],
      adresse: [this.patient.adresse],
      telephone: [this.patient.telephone]
    }, {
      updateOn: 'blur'
    });
  }


  onSave(){

    //console.log(this.patientForm.get('genre')?.errors);
    if (this.patientForm.invalid) return;
    const patientData: Patient = this.patientForm.value;

    if (this.mode === 'create') {

      const formValue = this.patientForm.value;

      // Convertir la date (ex: '2025-06-11') → objet Date → JSON
      const date = new Date(formValue.dateNaissance);
      const dateJson = date.toISOString(); // ← format JSON correct

      const patientData: Patient = {
        ...formValue,
        dateNaissance: dateJson  // remplace la date brute par format JSON
      };

      this.patientsService.addPatient(patientData).subscribe((response) => {
                console.log('Nouveau patient:', response);
            });
    } else if (this.mode === 'edit') {

      this.patientsService.updatePatient(patientData).subscribe((response) => {
                console.log('Patient mis à jour:', response);
        });
    }

    setTimeout(() => {
      this.router.navigate(['/liste-patients']);
    }, 100);

  }

  formatDateJsonForDatetimeLocal(dateString: string): string {
    const date = new Date(dateString);
    const offset = date.getTimezoneOffset();
    const localDate = new Date(date.getTime() - offset * 60 * 1000);
    return localDate.toISOString().slice(0, 10);
  }

}
