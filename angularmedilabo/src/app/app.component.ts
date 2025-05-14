import { Component, NgModule } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import {ReactiveFormsModule} from '@angular/forms';
import { PatientsService } from './services/patients.service';
import { CommonModule } from '@angular/common';
import { ListePatientsComponent } from './liste-patients/liste-patients.component';
import { DetailsPatientComponent } from './details-patient/details-patient.component';
import { BrowserModule } from '@angular/platform-browser';

/*@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})*/
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,HeaderComponent,ReactiveFormsModule,CommonModule],
  templateUrl: 'app.component.html',
  styleUrl: 'app.component.css'
})

/*@NgModule({
  declarations: [ListePatientsComponent,DetailsPatientComponent],
  imports:[BrowserModule],
  bootstrap: []
})*/
export class AppComponent {
  /*title = 'medilabo';*/
  patientsData: any;

  constructor(private patientsService: PatientsService) {}

  ngOnInit(): void {
    this.patientsService.getAllPatients().subscribe(
      data => {
        console.log(data);
        this.patientsData = data;
      },
      (error) => {
        console.error('Error fetching data:', error);
      }
    );
  }
}
