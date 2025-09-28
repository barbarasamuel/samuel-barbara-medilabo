package org.medilabo.micropatient.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.micropatient.dto.PatientsDTO;
import org.medilabo.micropatient.model.Patients;
import org.medilabo.micropatient.web.controller.PatientsController;
import org.medilabo.micropatient.web.exceptions.PatientIntrouvableException;
import org.medilabo.micropatient.web.service.PatientsService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientsControllerTest {

    private Date dateNaissance = new Date();
    @InjectMocks
    private PatientsController patientsController;

    @Mock
    private PatientsService patientsService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("anonymous");
        SecurityContextHolder.setContext(securityContext);

        dateNaissance = Date.from((LocalDate.now().minusYears(60).atStartOfDay(ZoneId.systemDefault()).toInstant()));

    }

    /**
     *
     * Should get patients list
     *
     */
    @Test
    void shouldGetPatientsListTest() {
        //Arrange
        Patients patient1 = new Patients();
        patient1.setId(200L);
        patient1.setPrenom("Jean");
        patient1.setNom("Dupont");
        patient1.setGenre("M");
        patient1.setDateNaissance(dateNaissance);

        Patients patient2 = new Patients();
        patient2.setId(201L);
        patient2.setPrenom("Marie");
        patient2.setNom("Curie");
        patient2.setGenre("F");
        patient2.setDateNaissance(dateNaissance);

        List<Patients> mockList = List.of(patient1, patient2);
        when(patientsService.findAll()).thenReturn(mockList);

        //Act
        ResponseEntity<List<Patients>> response = patientsController.listePatients();

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    /**
     *
     * Should print patient details
     *
     */
    @Test
    void shouldPrintPatientDetailTest() {
        //Arrange
        Patients patient1 = new Patients();
        patient1.setId(200L);
        patient1.setPrenom("Jean");
        patient1.setNom("Dupont");
        patient1.setGenre("M");
        patient1.setDateNaissance(dateNaissance);

        when(patientsService.findById(200L)).thenReturn(Optional.of(patient1));

        //Act
        Patients result = patientsController.afficherDetailPatient(200L);

        //Assert
        assertEquals("Jean", result.getPrenom());
    }

    /**
     *
     * Should not print patient details
     *
     */
    @Test
    void shouldNotPrintPatientDetailTest() {
        //Arrange
        when(patientsService.findById(99L)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(PatientIntrouvableException.class, () -> {
            patientsController.afficherDetailPatient(99L);
        });
    }

    /**
     *
     * Should add patient
     *
     */
    @Test
    void shouldAddPatientTest() {
        //Arrange
        Patients patient = new Patients();
        patient.setPrenom("Jean");
        patient.setNom("Dupont");
        patient.setGenre("M");
        patient.setDateNaissance(dateNaissance);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        //Act
        ResponseEntity<?> response = patientsController.ajouterPatient(patient);//,result);

        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }

    /**
     *
     * Should modify the patient
     *
     */
    @Test
    void shouldModifyPatientTest() {
        //Arrange
        Patients patient = new Patients();
        patient.setId(200L);
        patient.setPrenom("Jean");
        patient.setNom("Dupont");
        patient.setGenre("M");
        patient.setDateNaissance(dateNaissance);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        when(patientsService.findById(200L)).thenReturn(Optional.of(patient));
        when(patientsService.save(patient)).thenReturn(patient);
        patient.setAdresse("15 rue des alouettes");

        //Act
        ResponseEntity<?> response = patientsController.modifierPatient(200L, patient);//, result);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("15 rue des alouettes", ((Patients) response.getBody()).getAdresse());
    }

    /**
     *
     * Should not find the patient we want to modify
     *
     */
    @Test
    void shouldNotFindPatientTModifyTest() {
        //Arrange
        Patients patient = new Patients();
        patient.setId(200L);
        patient.setPrenom("Jean");
        patient.setNom("Dupont");
        patient.setGenre("M");
        patient.setDateNaissance(dateNaissance);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        when(patientsService.findById(200L)).thenReturn(Optional.empty());

        //Act
        ResponseEntity<?> response = patientsController.modifierPatient(200L, patient);//, result);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     *
     * Should get a patient
     *
     */
    @Test
    void shouldGetPatientTest() {
        //Arrange
        PatientsDTO dto = PatientsDTO.builder()
                .id(100L)
                .genre("M")
                .dateNaissance(dateNaissance)
                .build();

        when(patientsService.getPatientById("1")).thenReturn(dto);

        //Act
        ResponseEntity<PatientsDTO> response = patientsController.getPatient("1");

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("M", response.getBody().getGenre());
    }

    @Test
    void testGetPatient_notFound() {
        //Arrange
        when(patientsService.getPatientById("999")).thenThrow(new RuntimeException());

        //Act
        ResponseEntity<PatientsDTO> response = patientsController.getPatient("999");

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
