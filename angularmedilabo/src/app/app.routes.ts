import { Routes } from '@angular/router';
import { ListePatientsComponent } from './liste-patients/liste-patients.component';
import { DetailsPatientComponent } from './details-patient/details-patient.component';
import { ListeHistoComponent } from './liste-histo/liste-histo.component';
import { DetailsHistoComponent } from './details-histo/details-histo.component';
import { AccueilComponent } from './accueil/accueil.component';

export const routes: Routes = [
    { path: 'liste-patients', component: ListePatientsComponent },
    { path: 'details-patient', component: DetailsPatientComponent, data: { mode: 'create' } },
    { path: 'details-patient/:id', component: DetailsPatientComponent, data: { mode: 'edit' } },
    { path: 'details-patient/:id/view', component: DetailsPatientComponent, data: { mode: 'view' } },
    { path: 'liste-histo/:patId', component: ListeHistoComponent },
    { path: 'details-histo', component: DetailsHistoComponent, data: { mode: 'create' } },
    { path: 'details-histo/:id', component: DetailsHistoComponent, data: { mode: 'view' } },
    { path: '', component: AccueilComponent }
];
