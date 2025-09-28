package org.medilabo.micropatient.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.micropatient.dto.PatientsDTO;
import org.medilabo.micropatient.model.Patients;
import org.medilabo.micropatient.web.dao.PatientsRepository;
import org.medilabo.micropatient.web.service.PatientsService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientsServiceTest {
    private Date dateNaissance = new Date();

    @Mock
    private PatientsRepository patientsRepository;

    @InjectMocks
    private PatientsService patientsService;

    @BeforeEach
    void setUp() {
        dateNaissance = Date.from((LocalDate.now().minusYears(60).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    /**
     *
     * Should get all the patients list
     *
     */
    @Test
    void shouldGetAllThePatientListTest() {
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

        List<Patients> patientsList = Arrays.asList(patient1,patient2);
        when(patientsRepository.findAll()).thenReturn(patientsList);

        //Act
        List<Patients> result = patientsService.findAll();

        //Assert
        assertEquals(2, result.size());
        verify(patientsRepository, times(1)).findAll();
    }

    /**
     *
     * Should save a patient
     *
     */
    @Test
    void shouldSaveAPatientTest() {
        //Arrange
        Patients patient = new Patients();
        patient.setPrenom("Jean");
        patient.setNom("Dupont");
        patient.setGenre("M");
        patient.setDateNaissance(dateNaissance);
        patient.setAdresse("16 rue des alouettes");
        patient.setTelephone("555-444-3333");

        when(patientsRepository.save(any(Patients.class))).thenReturn(patient);

        //Act
        Patients result = patientsService.save(patient);

        //Assert
        assertNotNull(result);
        assertEquals("Jean", result.getPrenom());
        verify(patientsRepository, times(1)).save(patient);

        patientsRepository.deleteById(result.getId());
    }

    /**
     *
     * Should find patient by id
     *
     */
    @Test
    void shouldFindPatientByIdTest() {
        //Arrange
        Patients patient = new Patients();
        patient.setId(200L);
        patient.setPrenom("Jean");
        patient.setNom("Dupont");
        patient.setGenre("M");
        patient.setDateNaissance(dateNaissance);
        patient.setAdresse("16 rue des alouettes");
        patient.setTelephone("555-444-3333");

        when(patientsRepository.findById(200L)).thenReturn(Optional.of(patient));

        //Act
        Optional<Patients> result = patientsService.findById(200L);

        //Assert
        assertTrue(result.isPresent());
        assertEquals("16 rue des alouettes", result.get().getAdresse());
        verify(patientsRepository, times(1)).findById(200L);
    }

    /**
     *
     * Should return false when a patient is not found
     *
     */
    @Test
    void shouldReturnFalseWhenPatientNotFoundTest() {
        //Arrange
        when(patientsRepository.findById(200L)).thenReturn(Optional.empty());

        //Act
        Optional<Patients> result = patientsService.findById(200L);

        //Assert
        assertFalse(result.isPresent());
    }

    /**
     *
     * Should get a patient by id
     *
     */
    @Test
    void shouldGetAPatientByIdTest() {
        //Arrange
        Patients patient = new Patients();
        patient.setId(200L);
        patient.setPrenom("Jean");
        patient.setNom("Dupont");
        patient.setGenre("M");
        patient.setDateNaissance(dateNaissance);
        patient.setAdresse("16 rue des alouettes");
        patient.setTelephone("555-444-3333");

        when(patientsRepository.findById(200L)).thenReturn(Optional.of(patient));

        //Act
        PatientsDTO result = patientsService.getPatientById("200");

        //Assert
        assertNotNull(result);
        assertEquals(200L, result.getId());
        assertEquals(dateNaissance, result.getDateNaissance());
        assertEquals("M", result.getGenre());
    }

    /**
     *
     * Should return null because patient not found
     *
     */
    @Test
    void shouldReturnNullBecausePatientByIdNotFoundTest() {
        //Arrange
        when(patientsRepository.findById(1L)).thenReturn(Optional.empty());

        //Act
        PatientsDTO result = patientsService.getPatientById("1");

        //Assert
        assertNull(result);
    }
}
