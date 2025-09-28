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
  // Propriété pour stocker les erreurs
  validationErrors: { [key: string]: string } = {};

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

  onSave() {
    // Réinitialiser les erreurs précédentes
    this.validationErrors = {};
    
    const patientData: Patient = this.patientForm.value;

    if (this.mode === 'create') {
      const formValue = this.patientForm.value;

      // Convertir la date
      const date = new Date(formValue.dateNaissance);
      const dateJson = date.toISOString();

      const patientData: Patient = {
        ...formValue,
        dateNaissance: dateJson
      };

      this.patientsService.addPatient(patientData).subscribe({
        next: (response) => {
          console.log('Nouveau patient:', response);
          // Redirection seulement en cas de succès
          setTimeout(() => {
            this.router.navigate(['/liste-patients']);
          }, 100);
        },
        error: (error) => {
          console.error('Erreur lors de l\'ajout:', error);
          
          // Vérifier si c'est une erreur de validation (400)
          if (error.status === 400 && error.error) {
            this.validationErrors = error.error;
            console.log('Erreurs de validation:', this.validationErrors);
          } else {
            // Autres types d'erreurs
            this.validationErrors = { general: 'Une erreur est survenue lors de l\'ajout du patient.' };
          }
        }
      });
      
    } else if (this.mode === 'edit') {
      this.patientsService.updatePatient(patientData).subscribe({
        next: (response) => {
          console.log('Patient mis à jour:', response);
          // Redirection seulement en cas de succès
          setTimeout(() => {
            this.router.navigate(['/liste-patients']);
          }, 100);
        },
        error: (error) => {
          console.error('Erreur lors de la mise à jour:', error);
          
          if (error.status === 400 && error.error) {
            this.validationErrors = error.error;
          } else {
            this.validationErrors = { general: 'Une erreur est survenue lors de la mise à jour du patient.' };
          }
        }
      });
    }
  }

    // Méthode utilitaire pour récupérer l'erreur d'un champ spécifique
  getFieldError(fieldName: string): string | null {
    return this.validationErrors[fieldName] || null;
  }
  
  // Méthode pour vérifier si un champ a une erreur
  hasFieldError(fieldName: string): boolean {
    return !!this.validationErrors[fieldName];
  }

  formatDateJsonForDatetimeLocal(dateString: string): string {
    const date = new Date(dateString);
    const offset = date.getTimezoneOffset();
    const localDate = new Date(date.getTime() - offset * 60 * 1000);
    return localDate.toISOString().slice(0, 10);
  }

}
