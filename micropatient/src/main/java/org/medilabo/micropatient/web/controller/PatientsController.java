package org.medilabo.micropatient.web.controller;

import org.medilabo.micropatient.model.Patients;
import org.medilabo.micropatient.web.dao.PatientsRepository;
import org.medilabo.micropatient.web.exceptions.PatientIntrouvableException;
import org.medilabo.micropatient.web.service.PatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PatientsController {
    @Autowired
    private PatientsService patientsService;

    @GetMapping("/patients")
    public List<Patients> listePatients() {
        List<Patients> listePatients = patientsService.findAll();
        return listePatients;
    }

    @GetMapping("/patients/{id}")
    public Patients afficherDetailPatient(@PathVariable Long id) {
        Optional<Patients> patientAffiche = patientsService.findById(id);
        if(patientAffiche.get()==null) throw new PatientIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");
        //return "Vous avez demandé la fiche d'un patient avec l'id  " + id;
        return patientAffiche.get();
    }

    @PostMapping(value = "/patients")
    public Patients ajouterPatient(@RequestBody Patients patient) {
        Patients patientAjoute = patientsService.save(patient);
        /*if (patientAjoute == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(patientAjoute.getId())
                .toUri();*/
        //return ResponseEntity.created(location).build();
        return patientAjoute;
    }

    @PutMapping(value = "/patients/{id}")
    public ResponseEntity<Patients> modifierPatient(@RequestBody Patients patient,@PathVariable Long id) {
        Optional<Patients> existing = patientsService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Patients patientModifie = patientsService.save(patient);
        return ResponseEntity.ok(patientModifie);
    }


}
