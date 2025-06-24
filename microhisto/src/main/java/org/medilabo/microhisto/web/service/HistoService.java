package org.medilabo.microhisto.web.service;

import org.medilabo.microhisto.dto.HistoriqueDTO;
import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.dao.HistoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoService {
    @Autowired
    private HistoRepository histoRepository;

    /**
     *
     * To get the patient histos list
     *
     */
    public List<Histo> getHistoByPatient(Long id) {
        List<Histo> histo = histoRepository.findByPatId(id);
        return histo;
    }

    /**
     *
     * To get the histo details
     *
     */
    public Histo getNoteById(String id) {
        Optional<Histo> histo = histoRepository.findById(id);
        if(histo.isPresent()){
            return histo.get();
        }else{
            return null;
        }
    }

    /**
     *
     * To add a histo
     *
     */
    public Histo insert(Histo histo) {
        Histo newHisto = new Histo();
        newHisto.setPatId(histo.getPatId());
        newHisto.setPatient(histo.getPatient());//("TestNone");
        newHisto.setNote(histo.getNote());//("Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé");
        return histoRepository.insert(newHisto);
    }

    /**
     *
     * To get the patient histos
     *
     */
    public HistoriqueDTO getHistoriqueByPatientId(String patientId) {
        List<Histo> historique = histoRepository.findByPatId(Long.valueOf(patientId));

        List<String> notesList = new ArrayList<>();

        notesList = historique.stream()
                .map(Histo::getNote)
                .collect(Collectors.toList());

        HistoriqueDTO historiqueDto = new HistoriqueDTO();
        historiqueDto.setPatId(Long.valueOf(patientId));
        historiqueDto.setNote(notesList);

        return historiqueDto;
    }
}