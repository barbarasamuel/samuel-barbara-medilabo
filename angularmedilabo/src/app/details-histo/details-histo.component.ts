import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder,FormGroup,ReactiveFormsModule } from '@angular/forms';
import { HistoService } from '../services/histo.service';
import { ActivatedRoute,Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Histo } from '../models/histo';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-details-histo',
  standalone: true,
  imports: [ReactiveFormsModule,NgIf],
  templateUrl: './details-histo.component.html',
  styleUrl: './details-histo.component.css'
})
export class DetailsHistoComponent {
    histo$!: Observable<Histo>;
    histoForm!: FormGroup;
    @Input() mode: 'create' | 'edit' | 'view' = 'view';
    @Input() histoId?: string;//number;
    @Input() histo!: Histo;
    isReadOnly = false;
    responseData!: Histo;

    constructor(private router: Router, private formBuilder: FormBuilder,private route: ActivatedRoute,
      private histoService: HistoService) { 
        
    }
    
          ngOnInit(): void {
  
            const modeFromRoute = this.route.snapshot.data['mode'];
            if (modeFromRoute) {
              this.mode = modeFromRoute;
            }

            if (this.mode==="create") {
              const histoIdPat = String(this.route.snapshot.paramMap.get('patId'));
              const histoNomPat = this.route.snapshot.paramMap.get('nom');
              
              console.log(histoNomPat);
              this.histoForm = this.formBuilder.group({
                //id: [''],
                patId: [histoIdPat],
                patient: [histoNomPat],
                note: ['']
              });
            }
  
            if (this.mode==="view") { 
              const histoId = String(this.route.snapshot.paramMap.get('id'));
              this.histoService.getHistoById(histoId).subscribe(data =>{
                this.histo = data;
                this.initializeForm();
              });
            }
   
          }
  
          initializeForm(){
  
            this.histoForm = this.formBuilder.group({
              id: [this.histo.id],
              patId: [this.histo.patId],
              patient: [this.histo.patient],
              note: [this.histo.note]
            });

            this.histoForm.get('id')?.disable();
            this.histoForm.get('patId')?.disable();
            this.histoForm.get('patient')?.disable();
            this.histoForm.get('note')?.disable();
          }
  
          onSave() {
            if (this.histoForm.invalid) return;
  
            const noteData = this.histoForm.value;
   
            console.log(noteData);
            this.histo = noteData;  
            
            console.log(this.mode);
            if (this.mode === 'create') {
        
              console.log(this.histo);
              this.histoService.postAddHisto(this.histo).subscribe((response) => {
                console.log('Nouvelle note créée:', response);
                this.router.navigate(['/liste-histo', this.histo.patId, this.histo.patient]);
            });
            }
            
          }

          onGoBack(){
            if (this.histoForm.invalid) return;
  
            const noteData = this.histoForm.value;
            this.histo = noteData;
            this.router.navigate(['/liste-histo', this.histo.patId]);
          }
}
