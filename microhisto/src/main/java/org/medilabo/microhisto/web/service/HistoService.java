package org.medilabo.microhisto.web.service;

import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.dao.HistoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

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
        Optional<List<Histo>> histo = histoRepository.findByPatId(id);
        if(histo.isPresent()) {
            return histo.get();//histoRepository.findByPatient(histo.get().getPatient());
        }else{
            return null;
        }
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
}