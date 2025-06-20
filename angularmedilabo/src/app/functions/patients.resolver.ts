import { inject } from '@angular/core';
import { PatientsService } from '../services/patients.service';


export const patientsResolver = () => {
  const service = inject(PatientsService);
  return service.getAllPatients();
};
