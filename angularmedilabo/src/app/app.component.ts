import { Component } from '@angular/core';//, NgModule
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import {ReactiveFormsModule} from '@angular/forms';
import { PatientsService } from './services/patients.service';
import { CommonModule } from '@angular/common';
/*import { ListePatientsComponent } from './liste-patients/liste-patients.component';
import { DetailsPatientComponent } from './details-patient/details-patient.component';
import { BrowserModule } from '@angular/platform-browser';*/

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,HeaderComponent,ReactiveFormsModule,CommonModule],
  templateUrl: 'app.component.html',
  styleUrl: 'app.component.css'
})

export class AppComponent {
  
  patientsData: any;

  constructor(private patientsService: PatientsService) {}

  ngOnInit(): void {
    
    this.patientsService.getAllPatients().subscribe({
        next: (data) => { // 'next' est pour les données reçues avec succès
          console.log(data);
          this.patientsData = data;
        },
        error: (error) => { // 'error' est pour gérer les erreurs
          console.error('Error fetching data:', error);
        },
        complete: () => { // 'complete' est appelé quand l'observable est terminé (optionnel)
          console.log('Data fetching completed.');
        }
      });
      
    /*this.patientsService.getAllPatients().subscribe(
      data => {
        console.log(data);
        this.patientsData = data;
      },
      (error) => {
        console.error('Error fetching data:', error);
      }
    );*/
  }
}
