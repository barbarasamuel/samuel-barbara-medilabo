package org.medilabo.micropatient.web.service;

import org.medilabo.micropatient.dto.PatientsDTO;
import org.medilabo.micropatient.model.Patients;
import org.medilabo.micropatient.web.dao.PatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientsService {
    @Autowired
    private PatientsRepository patientsRepository;

    /**
     *
     * To get all the patients
     *
     */
    public List<Patients> findAll(){
        return patientsRepository.findAll();
    }


    /**
     *
     * To add or update a patient
     *
     */
    public Patients save(Patients patient){

        return patientsRepository.save(patient);
    }

    /**
     *
     * To get the details about a patient
     *
     */
    public Optional<Patients> findById(Long id){
        Optional<Patients> detailsPatient = patientsRepository.findById(id);
        return detailsPatient;
    }

    /**
     *
     * To get the id, the birthday and the gender of a patient
     *
     */
    public PatientsDTO getPatientById(String id) {
        Optional<Patients> optionalPatient = patientsRepository.findById(Long.valueOf(id));

        if(optionalPatient.isPresent()){
            PatientsDTO patientDTO = PatientsDTO.builder()
                    .id(optionalPatient.get().getId())
                    .dateNaissance(optionalPatient.get().getDateNaissance())
                    .genre(optionalPatient.get().getGenre())
                    .build();

            return patientDTO;
        }else{
            return null;
        }

    }

}
