import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import {ReactiveFormsModule} from '@angular/forms';
import { PatientsService } from './services/patients.service';
import { CommonModule } from '@angular/common';

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

  }
}
