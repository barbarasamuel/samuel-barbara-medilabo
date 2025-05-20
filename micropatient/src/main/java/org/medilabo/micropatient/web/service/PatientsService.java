package org.medilabo.micropatient.web.service;

import org.medilabo.micropatient.model.Patients;
import org.medilabo.micropatient.web.dao.PatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class PatientsService {
    @Autowired
    private PatientsRepository patientsRepository;

    /////////////////////////////////////////////
    private final RestTemplate restTemplate;

    public PatientsService() {
        this.restTemplate = new RestTemplate();
    }

    public String getUsers(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8999/patients",
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
    ////////////////////////////////////////////


    public List<Patients> findAll(){
        return patientsRepository.findAll();
    }


    public Patients save(Patients patient){

        return patientsRepository.save(patient);
    }

    public Optional<Patients> findById(Long id){
        Optional<Patients> detailsPatient = patientsRepository.findById(id);
        return detailsPatient;
    }
}
