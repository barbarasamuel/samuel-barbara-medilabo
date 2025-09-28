package org.medilabo.microhisto.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.microhisto.dto.HistoriqueDTO;
import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.controller.HistoController;
import org.medilabo.microhisto.web.service.HistoService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HistoControllerTest {
    @InjectMocks
    private HistoController histoController;

    @Mock
    private HistoService histoService;

    @BeforeEach
    void setUp() {
        // Simuler un utilisateur
        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.getName()).thenReturn("anonymous");

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Mockito.when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    /**
     *
     * Should return histo list
     *
     */
    @Test
    void shouldReturnHistoList() {
        //Arrange
        Histo histo1 = new Histo();
        histo1.setPatId(100L);
        histo1.setPatient("Dupont");
        histo1.setNote("S'est mis à fumer");

        Histo histo2 = new Histo();
        histo2.setPatId(100L);
        histo2.setPatient("Dupont");
        histo2.setNote("Continue à fumer");

        List<Histo> histosList = List.of(histo1,histo2);
        Mockito.when(histoService.getHistoByPatient(1L)).thenReturn(histosList);

        //Act
        List<Histo> result = histoController.getAllHistoByPatient("1");

        //Assert
        assertEquals(2, result.size());
    }

    /**
     *
     * Should return a note
     *
     */
    @Test
    void getNoteById_shouldReturnNote() {
        //Arrange
        Histo histo = new Histo();
        histo.setId("1");
        histo.setPatId(100L);
        histo.setPatient("Dupont");
        histo.setNote("S'est mis à fumer");

        Mockito.when(histoService.getNoteById("1")).thenReturn(histo);

        //Act
        Histo result = histoController.geNoteById("1");

        //Assert
        assertNotNull(result);
        assertEquals("Dupont",result.getPatient());
        assertEquals("S'est mis à fumer",result.getNote());
    }

    /**
     *
     * Should return a created histo
     *
     */
    @Test
    void shouldReturnCreatedHisto() {
        //Arrange
        Histo histo = new Histo();
        histo.setPatId(100L);
        histo.setPatient("Dupont");
        histo.setNote("S'est mis à fumer");

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        Mockito.when(histoService.insert(histo)).thenReturn(histo);

        //Act
        ResponseEntity<?> response = histoController.insert(histo);//, result);//

        //Assert
        assertEquals(histo, response.getBody());
    }

    /**
     *
     * Should return a HistoriqueDTO
     *
     */
    @Test
    void shouldReturnDTO() {
        //Arrange
        HistoriqueDTO dtoHisto = new HistoriqueDTO();
        dtoHisto.setPatId(1L);
        dtoHisto.setNote(List.of("Est fumeur","A eu des réactions"));

        Mockito.when(histoService.getHistoriqueByPatientId("1")).thenReturn(dtoHisto);

        //Act
        ResponseEntity<HistoriqueDTO> response = histoController.getHistorique("1");

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtoHisto, response.getBody());
        assertEquals(2, response.getBody().getNote().size());
    }

    /**
     *
     * Should return NotFound when exception
     *
     */
    @Test
    void shouldReturnNotFound_whenException() {
        //Arrange
        Mockito.when(histoService.getHistoriqueByPatientId("1")).thenThrow(new RuntimeException());

        //Act
        ResponseEntity<HistoriqueDTO> response = histoController.getHistorique("1");

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
