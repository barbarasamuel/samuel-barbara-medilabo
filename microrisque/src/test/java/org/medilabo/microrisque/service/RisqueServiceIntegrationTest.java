package org.medilabo.microrisque.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.microrisque.config.MicroHistoClient;
import org.medilabo.microrisque.config.MicroPatientClient;
import org.medilabo.microrisque.model.HistoriqueDTO;
import org.medilabo.microrisque.model.PatientsDTO;
import org.medilabo.microrisque.web.service.RisqueService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RisqueServiceIntegrationTest {
    @Mock
    private MicroPatientClient microPatientClient;

    @Mock
    private MicroHistoClient microHistoClient;

    @InjectMocks
    private RisqueService risqueService;

    private PatientsDTO mockPatient(String genre, LocalDate naissance) {
        PatientsDTO patient = new PatientsDTO();
        patient.setGenre(genre);
        patient.setDateNaissance(Date.from(naissance.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return patient;
    }

    private HistoriqueDTO mockHisto(List<String> notes) {
        HistoriqueDTO historique = new HistoriqueDTO();
        historique.setNote(notes);
        return historique;
    }

    /**
     *
     * Should return EarlyOnset for man is less than 30 and 5 keywords
     *
     */
    @Test
    void shouldReturnEarlyOnsetForManIsLessThan30AndHas5Keywords() {
        //Arrange
        String patientId = "100";
        PatientsDTO patient = mockPatient("M", LocalDate.now().minusYears(25));
        HistoriqueDTO histo = mockHisto(Arrays.asList("Hémoglobine A1C", "Microalbumine", "Taille", "Poids", "Fumeur"));

        when(microPatientClient.getPatient(100L)).thenReturn(ResponseEntity.ok(patient));
        when(microHistoClient.getHistorique(patientId)).thenReturn(ResponseEntity.ok(histo));

        //Act
        String result = risqueService.evaluerRisque(patientId);

        //Assert
        assertEquals("Apparition précoce (Early onset)", result);
    }

    /**
     *
     * Should return Danger for woman is less than 30 old and 4 keywords
     *
     */
    @Test
    void shouldReturnDangerForWomanIsLessThan30And4Keywords() {
        //Arrange
        String patientId = "100";
        PatientsDTO patient = mockPatient("F", LocalDate.now().minusYears(28));
        HistoriqueDTO historique = mockHisto(Arrays.asList("Hémoglobine A1C", "Microalbumine", "Taille", "Poids"));

        when(microPatientClient.getPatient(100L)).thenReturn(ResponseEntity.ok(patient));
        when(microHistoClient.getHistorique(patientId)).thenReturn(ResponseEntity.ok(historique));

        //Act
        String result = risqueService.evaluerRisque(patientId);

        //Assert
        assertEquals("Danger (In Danger)", result);
    }

    /**
     *
     * Should return Borderline for a woman is 45 and between 2 and 5 keywords
     *
     */
    @Test
    void shouldReturnBorderlineForWomanIs45AndBetween2And5Keywords() {
        //Arrange
        String patientId = "100";
        PatientsDTO patient = mockPatient("F", LocalDate.now().minusYears(45));
        HistoriqueDTO historique = mockHisto(Arrays.asList("Taille", "Poids", "Vertiges"));

        when(microPatientClient.getPatient(100L)).thenReturn(ResponseEntity.ok(patient));
        when(microHistoClient.getHistorique(patientId)).thenReturn(ResponseEntity.ok(historique));

        //Act
        String result = risqueService.evaluerRisque(patientId);

        //Assert
        assertEquals("Risque limité (Borderline)", result);
    }

    /**
     *
     * Should return Aucun risque for a woman is 35 and 0 keyword
     *
     */
    @Test
    void shouldReturnAucunRisqueForWomanIs35And0Keyword() {
        //Arrange
        String patientId = "100";
        PatientsDTO patient = mockPatient("F", LocalDate.now().minusYears(35));
        HistoriqueDTO historique = mockHisto(Arrays.asList("Tout va bien"));

        when(microPatientClient.getPatient(100L)).thenReturn(ResponseEntity.ok(patient));
        when(microHistoClient.getHistorique(patientId)).thenReturn(ResponseEntity.ok(historique));

        //Act
        String result = risqueService.evaluerRisque(patientId);

        //Assert
        assertEquals("Aucun risque (None)", result);
    }

    /**
     *
     * Should return Throw when patient not found
     *
     */
    @Test
    void shouldReturnThrowWhenPatientNotFound() {
        //Arrange
        when(microPatientClient.getPatient(999L)).thenReturn(ResponseEntity.notFound().build());

        //Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            risqueService.evaluerRisque("999");
        });

        assertTrue(exception.getMessage().contains("Patient non trouvé"));
    }

    /**
     *
     * Should return Throw when historique not found
     *
     */
    @Test
    void shouldReturnThrowWhenHistoriqueNotFound() {
        //Arrange
        PatientsDTO patient = mockPatient("F", LocalDate.now().minusYears(35));
        when(microPatientClient.getPatient(100L)).thenReturn(ResponseEntity.ok(patient));
        when(microHistoClient.getHistorique("100")).thenReturn(ResponseEntity.notFound().build());

        //Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            risqueService.evaluerRisque("100");
        });

        assertTrue(exception.getMessage().contains("Historique non trouvé"));
    }

    /**
     *
     * Should return Aucun risque if without note
     *
     */
    @Test
    void shouldReturnAucunRisqueIfWithoutNote() {
        //Arrange
        String patientId = "100";
        PatientsDTO patient = mockPatient("F", LocalDate.now().minusYears(50));
        HistoriqueDTO histo = mockHisto(null);

        when(microPatientClient.getPatient(100L)).thenReturn(ResponseEntity.ok(patient));
        when(microHistoClient.getHistorique(patientId)).thenReturn(ResponseEntity.ok(histo));

        //Act
        String result = risqueService.evaluerRisque(patientId);

        //Assert
        assertEquals("Aucun risque (None)", result);
    }

}
