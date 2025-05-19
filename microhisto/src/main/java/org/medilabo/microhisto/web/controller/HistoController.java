package org.medilabo.microhisto.web.controller;

import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.service.HistoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/histo")
public class HistoController {
    @Autowired
    private HistoService histoService;

    @GetMapping("/Hist/{id}")
    public List<Histo> getAllHistoByPatient(@PathVariable String id) {
        return histoService.getHistoByPatient(Long.valueOf(id));
    }

    @GetMapping("/Hist/details/{id}")
    public Histo geNoteById(@PathVariable String id) {
        return histoService.getNoteById(id);
    }

    @PostMapping(value = "/Hist/creation/{id}")
    public Histo insert(@RequestBody Histo histo, @PathVariable String id) {
        return histoService.insert(histo,id);
    }

}
