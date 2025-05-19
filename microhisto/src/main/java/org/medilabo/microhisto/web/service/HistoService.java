package org.medilabo.microhisto.web.service;

import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.dao.HistoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoService {
    @Autowired
    private HistoRepository histoRepository;

    public List<Histo> getHistoByPatient(Long id) {
        Optional<List<Histo>> histo = histoRepository.findByPatId(id);
        if(histo.isPresent()) {
            return histo.get();//histoRepository.findByPatient(histo.get().getPatient());
        }else{
            return null;
        }
    }

    public Histo getNoteById(String id) {
        return histoRepository.findById(Long.valueOf(id)).orElse(null);
    }

    public Histo insert(Histo histo, String id) {
        Histo newHisto = new Histo();
        newHisto.setPatId(Long.valueOf(id));
        newHisto.setPatient(histo.getPatient());//("TestNone");
        newHisto.setNote(histo.getNote());//("Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé");
        return histoRepository.insert(histo);
    }
}