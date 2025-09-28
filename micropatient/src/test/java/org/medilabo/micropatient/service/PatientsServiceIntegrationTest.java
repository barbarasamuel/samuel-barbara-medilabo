package org.medilabo.micropatient.service;

import org.junit.jupiter.api.Test;
import org.medilabo.micropatient.model.Patients;
import org.medilabo.micropatient.web.dao.PatientsRepository;
import org.medilabo.micropatient.web.service.PatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class PatientsServiceIntegrationTest {
    @Autowired
    private PatientsService patientsService;

    @Autowired
    private PatientsRepository patientsRepository;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    /**
     *
     * Should save a updated patient
     *
     */
    @Test
    void shouldSaveAUpdatedPatientTest() {
        //Arrange
        Date dateNaissance = new Date();
        dateNaissance = Date.from((LocalDate.now().minusYears(60).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Patients patient = new Patients();
        patient.setPrenom("Jean");
        patient.setNom("Dupont");
        patient.setGenre("M");
        patient.setDateNaissance(dateNaissance);
        patient.setAdresse("16 rue des alouettes");
        patient.setTelephone("555-444-3333");

        Patients resultSavedPatient = patientsService.save(patient);

        patient.setPrenom("Jean");
        patient.setNom("Dupont");
        patient.setGenre("M");
        patient.setDateNaissance(dateNaissance);
        patient.setAdresse("6 rue du parc");
        patient.setTelephone("888-444-2222");

        //Act
        Patients resultUpdatedPatient = patientsService.save(patient);

        //Assert
        assertNotNull(resultUpdatedPatient);
        assertEquals("Jean", resultUpdatedPatient.getPrenom());
        assertEquals("6 rue du parc", resultUpdatedPatient.getAdresse());
        assertEquals("888-444-2222", resultUpdatedPatient.getTelephone());

        patientsRepository.deleteById(resultUpdatedPatient.getId());
    }
}
