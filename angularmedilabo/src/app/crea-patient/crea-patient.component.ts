import { Component, OnInit } from '@angular/core';
import { FormBuilder,FormGroup,Validators } from '@angular/forms';

@Component({
  selector: 'app-crea-patient',
  standalone: true,
  imports: [],
  templateUrl: './crea-patient.component.html',
  styleUrl: './crea-patient.component.css'
})
export class CreaPatientComponent implements OnInit {
  patientForm!: FormGroup;
  //nomPrenomRegExp!: RegExp;

  //constructor() { }
  constructor(private formBuilder: FormBuilder) { }

    ngOnInit(): void {
      //this.nomPrenomRegExp = 
      this.patientForm = this.formBuilder.group({
          //id: [null],
          nom: [null,[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
          prenom: [null,[Validators.required,Validators.pattern('^[a-zA-Z]+$')]],
          dateNaissance: [null,[Validators.pattern('^(19\d{2}|20\d{2})$)-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$')]],
          genre: [null,[Validators.required]],
          adresse: [null],
          telephone: [null, [Validators.pattern('^\d{3}-\d{3}-\d{4}$')]]
      }, {
        updateOn: 'blur'
      });
  }

  onSubmitForm() {
    console.log(this.patientForm.value);
  }
}


