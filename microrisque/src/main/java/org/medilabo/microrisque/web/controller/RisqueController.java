package org.medilabo.microrisque.web.controller;

import org.medilabo.microrisque.web.service.RisqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RisqueController {
    @Autowired
    private RisqueService risqueService;

    @PostMapping
    public Map<String, Integer> analyze(@RequestBody List<String> terms) {
        return risqueService.countTerms(terms);
    }
}
