import { Component, Input, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormBuilder,FormGroup,Validators,ReactiveFormsModule } from '@angular/forms';
import { PatientsService } from '../services/patients.service';
import { ActivatedRoute,Router } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Patient } from '../models/patient';
import { CommonModule,JsonPipe,NgIf } from '@angular/common'; 

@Component({
  selector: 'app-details-patient',
  standalone: true,
  imports: [RouterOutlet,ReactiveFormsModule,NgIf,JsonPipe],
  templateUrl: './details-patient.component.html',
  styleUrl: './details-patient.component.css'
})
export class DetailsPatientComponent implements OnInit{
  patients$!: Observable<Patient>;
  //patient!: Patient;// | null;// = null;
  //patient!:Patient;
  patientForm!: FormGroup;
  @Input() mode: 'create' | 'edit' | 'view' = 'view';
  @Input() patientId?: number;
  @Input() patient!: Patient;
  isReadOnly = false;
    
  constructor(private router: Router, private formBuilder: FormBuilder,private route: ActivatedRoute,
    private patientsService: PatientsService) { 
      //this.patients$ = this.patientsService.patients$;
    /*this.patientForm = this.formBuilder.group({
      id: [],
      nom: [''],
      prenom: [''],
      dateNaissance: [''],
      genre: [''],
      adresse: [''],
      telephone: ['']
  });*/
  }
  /*ngOnInit(): void {
  this.patientForm = this.formBuilder.group({
            id: [null],
            nom: [null,[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
            prenom: [null,[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
            dateNaissance: [null,[Validators.pattern('^(19\d{2}|20\d{2})$)-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$')]],
            genre: [null,[Validators.required]],
            adresse: [null],
            telephone: [null, [Validators.pattern('^\d{3}-\d{3}-\d{4}$')]]
        }, {
          updateOn: 'blur'
        });
    }*/
        ngOnInit(): void {

          const modeFromRoute = this.route.snapshot.data['mode'];
          if (modeFromRoute) {
            this.mode = modeFromRoute;
          }

          this.patientForm = this.formBuilder.group({
            nom: ['',[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
            prenom: ['',[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
            dateNaissance: ['',[Validators.pattern('^(19|20)\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$')]],
            genre: ['M',[Validators.required]],
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
          

          
/*this.patientForm = this.formBuilder.group({
            nom: [this.patient.nom,[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
            prenom: [this.patient.prenom,[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
            dateNaissance: [this.patient.dateNaissance,[Validators.pattern('^(19|20)\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$')]],
            genre: [this.patient.genre,[Validators.required]],
            adresse: [this.patient.adressePostale],
            telephone: [this.patient.numTel, [Validators.pattern('^\d{3}-\d{3}-\d{4}$')]]
          }, {
            updateOn: 'blur'
          }); */
          this.isReadOnly = this.mode === 'view';

          if (this.mode !== 'create' && this.patientId != null) {
            /*this.patientsService.getPatientById(this.patientId).subscribe((data) => {
              this.patient = data;
              this.patientForm.patchValue(this.patient);
            });*/
          }
          
        }

        initializeForm(){

          this.patientForm = this.formBuilder.group({
            nom: [this.patient.nom],
            prenom: [this.patient.prenom],
            dateNaissance: [this.patient.dateNaissance],
            genre: [this.patient.genre],
            adresse: [this.patient.adressePostale],
            telephone: [this.patient.numTel]
          }, {
            updateOn: 'blur'
          });
        }

        onSave() {
          if (this.patientForm.invalid) return;

          const patientData = this.patientForm.value;
 
              
          if (this.mode === 'create') {
            //this.patient.id = Date.now();
            this.patientsService.addPatient(this.patient);
            console.log('Nouveau patient créé:', patientData);
          } else if (this.mode === 'edit') {
            this.patientsService.updatePatient(this.patient);
            console.log('Patient mis à jour:', patientData);
          }

          this.router.navigate(['/liste-patients']);
        }
}
