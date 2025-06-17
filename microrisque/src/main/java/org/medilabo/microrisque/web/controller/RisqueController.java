package org.medilabo.microrisque.web.controller;

import org.medilabo.microrisque.web.service.RisqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class RisqueController {
    @Autowired
    private RisqueService risqueService;

    /**
     *
     * To communicate the evaluated risk
     *
     */
    @GetMapping("/evaluer/{patientId}")
    public ResponseEntity<String> evaluerRisque(@PathVariable String patientId) {
        try {
            String resultat = risqueService.evaluerRisque(patientId);

            return ResponseEntity.ok(resultat);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'évaluation du risque : " + e.getMessage());

        }
    }

}
