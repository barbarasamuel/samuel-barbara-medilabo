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
    // Propriété pour stocker les erreurs
    validationErrors: { [key: string]: string } = {};

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
            // Réinitialiser les erreurs précédentes
            this.validationErrors = {};
            
            const noteData = this.histoForm.value;

            console.log(noteData);
            this.histo = noteData;  
            
            console.log(this.mode);
            if (this.mode === 'create') {

              console.log(this.histo);
              this.histoService.postAddHisto(this.histo).subscribe({
                next: (response) => {
                  console.log('Nouvelle note créée:', response);
                  // Redirection seulement en cas de succès
                  this.router.navigate(['/liste-histo', this.histo.patId, this.histo.patient]);
                },
                error: (error) => {
                  console.error('Erreur lors de l\'ajout de la note:', error);
                  
                  // Vérifier si c'est une erreur de validation (400)
                  if (error.status === 400 && error.error) {
                    this.validationErrors = error.error;
                    console.log('Erreurs de validation:', this.validationErrors);
                  } else {
                    // Autres types d'erreurs
                    this.validationErrors = { general: 'Une erreur est survenue lors de l\'ajout de la note.' };
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

          onGoBack(){
            if (this.histoForm.invalid) return;
  
            const noteData = this.histoForm.value;
            this.histo = noteData;
            this.router.navigate(['/liste-histo', this.histo.patId]);
          }
}
