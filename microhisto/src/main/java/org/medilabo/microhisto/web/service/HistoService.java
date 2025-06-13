package org.medilabo.microhisto.web.service;

import org.medilabo.microhisto.dto.HistoriqueDTO;
import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.dao.HistoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoService {
    @Autowired
    private HistoRepository histoRepository;

    /////////////////////////////////////////////
    private final RestTemplate restTemplate;

    public HistoService() {
        this.restTemplate = new RestTemplate();
    }

    public String getUsers(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8999/hist",
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
    ////////////////////////////////////////////

    public List<Histo> getHistoByPatient(Long id) {
        List<Histo> histo = histoRepository.findByPatId(id);
        return histo;
    }

    public Histo getNoteById(String id) {
        Optional<Histo> histo = histoRepository.findById(id);
        if(histo.isPresent()){
            return histo.get();
        }else{
            return null;
        }
    }

    public Histo insert(Histo histo) {
        Histo newHisto = new Histo();
        newHisto.setPatId(histo.getPatId());
        newHisto.setPatient(histo.getPatient());//("TestNone");
        newHisto.setNote(histo.getNote());//("Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé");
        return histoRepository.insert(newHisto);
    }

    public HistoriqueDTO getHistoriqueByPatientId(String patientId) {
        List<Histo> historique = histoRepository.findByPatId(Long.valueOf(patientId));

        List<String> notesList = new ArrayList<>();

        if (historique == null || historique.isEmpty()) {
            throw new RuntimeException("Historique pour le patient " + patientId + " non trouvé");
        }

        notesList = historique.stream()
                .map(Histo::getNote)
                .collect(Collectors.toList());

        HistoriqueDTO historiqueDto = new HistoriqueDTO();
        historiqueDto.setPatId(Long.valueOf(patientId));
        historiqueDto.setNote(notesList);

        return historiqueDto;
    }
}