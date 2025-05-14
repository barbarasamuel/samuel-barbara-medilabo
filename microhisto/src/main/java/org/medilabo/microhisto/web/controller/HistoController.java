package org.medilabo.microhisto.web.controller;

import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.service.HistoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/histo")
public class HistoController {
    @Autowired
    private HistoService histoService;

    @GetMapping("/Histo/{id}")
    public List<Histo> getAllHistoById(@PathVariable Histo histo) {
        return histoService.getHistoById(histo);
    }

    @GetMapping("/Histo/details/{id}")
    public Histo geNoteById(@PathVariable String id) {
        return histoService.getNoteById(id);
    }

    @PostMapping(value = "/Histo")
    public Histo save(@RequestBody Histo histo) {
        return histoService.save(histo);
    }

}
