package org.medilabo.microhisto.web.controller;

import org.medilabo.microhisto.dto.HistoriqueDTO;
import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.service.HistoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hist")
//@CrossOrigin(origins = {"http://localhost","http://localhost:8080","http://localhost:4200"})
public class HistoController {
    @Autowired
    private HistoService histoService;

    /**
     *
     * To display the list of the patient histos
     *
     */
    //@GetMapping("/hist/{id}")
    @GetMapping("/{id}")
    public List<Histo> getAllHistoByPatient(@PathVariable String id) {
        return histoService.getHistoByPatient(Long.valueOf(id));
    }

    /**
     *
     * To get the histo note
     *
     */
    //@GetMapping("/hist/details/{id}")
    @GetMapping("/details/{id}")
    public Histo geNoteById(@PathVariable String id) {
        return histoService.getNoteById(id);
    }

    /**
     *
     * To add a histo
     *
     */
    //@PostMapping(value = "/hist/creation")//@PostMapping(value = "/hist/creation/{id}")
    @PostMapping(value = "/creation")
    public Histo insert(@RequestBody Histo histo) {
        return histoService.insert(histo);
    }

    /**
     *
     * To get all the patient histos
     *
     */
    //@GetMapping("/hist/risque/{patientId}")
    @GetMapping("/risque/{patientId}")
    public ResponseEntity<HistoriqueDTO> getHistorique(@PathVariable String patientId) {
        try {
            HistoriqueDTO historique = histoService.getHistoriqueByPatientId(patientId);
            return ResponseEntity.ok(historique);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
