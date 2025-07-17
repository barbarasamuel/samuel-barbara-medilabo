package org.medilabo.micropatient.web.controller;

import org.medilabo.micropatient.dto.PatientsDTO;
import org.medilabo.micropatient.model.Patients;
import org.medilabo.micropatient.web.exceptions.PatientIntrouvableException;
import org.medilabo.micropatient.web.service.PatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
//@CrossOrigin(origins = {"http://localhost","http://localhost:8080","http://localhost:4200"})
public class PatientsController {
    @Autowired
    private PatientsService patientsService;

    /**
     *
     * To list all the patients
     *
     */
    //@GetMapping("/patients")
    @GetMapping("")
    public List<Patients> listePatients() {
        List<Patients> listePatients = patientsService.findAll();
        return listePatients;
    }

    /**
     *
     * To display the details of a patient
     *
     */
    //@GetMapping("/patients/{id}")
    @GetMapping("/{id}")
    public Patients afficherDetailPatient(@PathVariable Long id) {
        Optional<Patients> patientAffiche = patientsService.findById(id);
        if(patientAffiche.get()==null) throw new PatientIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");
        //return "Vous avez demandé la fiche d'un patient avec l'id  " + id;
        return patientAffiche.get();
    }

    /**
     *
     * To add a patient
     *
     */
    //@PostMapping(value = "/patients")
    @PostMapping("")
    public ResponseEntity<Patients> ajouterPatient(@RequestBody Patients patient) {
        Patients patientAjoute = patientsService.save(patient);
        if (patientAjoute == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(patientAjoute.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }

    /**
     *
     * To update a patient
     *
     */
    //@PutMapping(value = "/patients/{id}")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Patients> modifierPatient(@RequestBody Patients patient,@PathVariable Long id) {
        Optional<Patients> existing = patientsService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Patients patientModifie = patientsService.save(patient);
        return ResponseEntity.ok(patientModifie);
    }

    /**
     *
     * To get the patient id, birthday and gender
     *
     */
    @GetMapping("/info/{id}")
    public ResponseEntity<PatientsDTO> getPatient(@PathVariable String id) {
        try {
            PatientsDTO patient = patientsService.getPatientById(id);
            return ResponseEntity.ok(patient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
