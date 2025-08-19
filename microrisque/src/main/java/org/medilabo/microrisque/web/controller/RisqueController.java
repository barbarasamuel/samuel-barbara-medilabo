package org.medilabo.microrisque.web.controller;

import org.medilabo.microrisque.web.service.RisqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
@RequestMapping("/evaluer")
@CrossOrigin(origins = {"http://localhost","http://localhost:8080","http://localhost:4200"})
public class RisqueController {
    @Autowired
    private RisqueService risqueService;

    /**
     *
     * To communicate the evaluated risk
     *
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<String> evaluerRisque(@PathVariable String patientId) {
        try {
            // Récupérer l'utilisateur authentifié
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            //System.out.println("Request from user: " + username);

            return ResponseEntity.ok(risqueService.evaluerRisque(patientId));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'évaluation du risque : " + e.getMessage());

        }
    }

}
