package org.medilabo.microrisque.config;

import org.medilabo.microrisque.model.PatientsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "micropatient", url = "${micropatient.url:http://localhost:8999}")
public interface MicroPatientClient {
    @GetMapping("/patients/{id}")
    ResponseEntity<PatientsDTO> getPatient(@PathVariable Long id);
}
