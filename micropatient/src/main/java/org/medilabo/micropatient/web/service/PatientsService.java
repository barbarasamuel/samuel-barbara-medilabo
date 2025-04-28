package org.medilabo.micropatient.web.service;

import org.medilabo.micropatient.model.Patients;
import org.medilabo.micropatient.web.dao.PatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class PatientsService {
    @Autowired
    private PatientsRepository patientsRepository;

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
