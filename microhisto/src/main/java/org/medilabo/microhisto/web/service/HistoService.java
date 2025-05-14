package org.medilabo.microhisto.web.service;

import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.dao.HistoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoService {
    @Autowired
    private HistoRepository histoRepository;

    public List<Histo> getHistoById(Histo histo) {
        return histoRepository.findAllById(histo.getPatId());
    }

    public Histo getNoteById(String id) {
        return histoRepository.findById(id).orElse(null);
    }

    public Histo save(Histo histo) {
        return histoRepository.save(histo);

    }
}