import { Component,OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { HistoService } from '../services/histo.service';
import { Observable } from 'rxjs';
import { Histo } from '../models/histo';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-liste-histo',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './liste-histo.component.html',
  styleUrl: './liste-histo.component.css'
})

export class ListeHistoComponent  implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  patId !: number;
  nom ='';
  histo$!: Observable<Histo[]>;
  histo: any[] = [];
  
  histoForm!: FormGroup;
   
  constructor(private formBuilder: FormBuilder, private histoService: HistoService) { 
  } 

  ngOnInit() {
    // Appel de la méthode du service
    this.route.params.subscribe(params => {
       this.patId = +params['id']; // + permet de convertir la chaîne en nombre
        this.nom = params['nom'];
        this.histo$ = this.histoService.getAllHistoPatient(this.patId);
      });

    //Rechargement de la liste
    this.loadHistos();
  }

  loadHistos() {
    this.histoService.getAllHistoPatient(this.patId).subscribe(data => {
      this.histo = data;
    });
  }

  onCreateNote(histoIdPat: number, histoNomPat: string) {
    this.router.navigate(['/details-histo',histoIdPat ,histoNomPat]);
    
    //Rechargement de la liste
    this.loadHistos();
  }

  onOpenNote(histoId: string): void {
    this.router.navigate(['/details-histo',histoId]);
  }

}
