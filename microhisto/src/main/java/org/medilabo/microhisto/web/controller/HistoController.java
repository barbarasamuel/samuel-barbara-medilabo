package org.medilabo.microhisto.web.controller;

import org.medilabo.microhisto.dto.HistoriqueDTO;
import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.service.HistoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class HistoController {
    @Autowired
    private HistoService histoService;

    @GetMapping("/hist/{id}")
    public List<Histo> getAllHistoByPatient(@PathVariable String id) {
        return histoService.getHistoByPatient(Long.valueOf(id));
    }

    @GetMapping("/hist/details/{id}")
    public Histo geNoteById(@PathVariable String id) {
        return histoService.getNoteById(id);
    }

    @PostMapping(value = "/hist/creation")//@PostMapping(value = "/hist/creation/{id}")
    public Histo insert(@RequestBody Histo histo) {
        return histoService.insert(histo);
    }

    @GetMapping("/hist/risque/{patientId}")
    public ResponseEntity<HistoriqueDTO> getHistorique(@PathVariable String patientId) {
        try {
            HistoriqueDTO historique = histoService.getHistoriqueByPatientId(patientId);
            return ResponseEntity.ok(historique);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
