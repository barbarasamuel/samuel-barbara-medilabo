package org.medilabo.microhisto.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.microhisto.dto.HistoriqueDTO;
import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.dao.HistoRepository;
import org.medilabo.microhisto.web.service.HistoService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HistoServiceTest {
    @Mock
    private HistoRepository histoRepository;

    @InjectMocks
    private HistoService histoService;

    private Histo histo1;
    private Histo histo2;

    @BeforeEach
    public void setUp() {
        histo1 = new Histo();
        histo1.setId("1");
        histo1.setPatId(100L);
        histo1.setNote("S'est mis à fumer");

        histo2 = new Histo();
        histo2.setId("2");
        histo2.setPatId(100L);
        histo2.setNote("Est devenu fumeur");
    }

    @AfterEach
    public void endedTest(){
        histoRepository.deleteByPatIdAndNote(100L, "S'est mis à fumer");
    }


    /**
     *
     * Should get histo by patient
     *
     */
    @Test
    public void shouldGetHistoByPatientTest() {
        //Arrange
        when(histoRepository.findByPatId(100L)).thenReturn(List.of(histo1, histo2));

        //Act
        List<Histo> result = histoService.getHistoByPatient(100L);

        //Assert
        assertEquals(2, result.size());
        assertEquals("S'est mis à fumer", result.get(0).getNote());
        verify(histoRepository, times(1)).findByPatId(100L);
    }

    /**
     *
     * Should get note by id
     *
     */
    @Test
    public void shouldGetNoteByIdTest() {
        //Arrange
        when(histoRepository.findById("1")).thenReturn(Optional.of(histo1));

        //Act
        Histo result = histoService.getNoteById("1");

        //Assert
        assertNotNull(result);
        assertEquals("S'est mis à fumer", result.getNote());
        verify(histoRepository, times(1)).findById("1");
    }

    /**
     *
     * Should not find note
     *
     */
    @Test
    public void shouldNotFindNoteByIdTest() {
        //Arrange
        when(histoRepository.findById("99")).thenReturn(Optional.empty());

        //Act
        Histo result = histoService.getNoteById("99");

        //Assert
        assertNull(result);
        verify(histoRepository, times(1)).findById("99");
    }

    /**
     *
     * Should insert
     *
     */
    @Test
    public void shouldInsertTest() {
        //Arrange
        Histo inputHisto = new Histo();
        inputHisto.setPatId(100L);
        inputHisto.setNote("A eu une réaction");
        inputHisto.setPatient("Dupont");

        Histo savedHisto = new Histo();
        savedHisto.setPatId(100L);
        savedHisto.setNote("Deuxième réaction");
        savedHisto.setPatient("Dupont");

        when(histoRepository.insert(any(Histo.class))).thenReturn(savedHisto);

        //Act
        Histo result = histoService.insert(inputHisto);

        //Assert
        assertEquals("Deuxième réaction", result.getNote());
        assertEquals(100L, result.getPatId());
        verify(histoRepository, times(1)).insert(any(Histo.class));

        histoRepository.deleteByPatId(100L);
        List<Histo> histoList = histoRepository.findByPatId(100L);
        assertEquals(0, histoList.size());
    }

    /**
     *
     * Should get historique by patientId
     *
     */
    @Test
    public void shouldGetHistoriqueByPatientIdTest() {
        //Arrange
        when(histoRepository.existsByPatId(100L)).thenReturn(true);
        when(histoRepository.findByPatId(100L)).thenReturn(List.of(histo1, histo2));

        //Act
        HistoriqueDTO result = histoService.getHistoriqueByPatientId("100");

        //Assert
        assertEquals(100L, result.getPatId());
        assertEquals(2, result.getNote().size());
        assertEquals("S'est mis à fumer", result.getNote().get(0));
        verify(histoRepository).existsByPatId(100L);
        verify(histoRepository).findByPatId(100L);
    }

    /**
     *
     * Should not find historique by patientId
     *
     */
    @Test
    public void shouldNotFindHistoriqueByPatientIdTest() {
        //Arrange and Act
        when(histoRepository.existsByPatId(999L)).thenReturn(false);

        //Assert
        assertThrows(NoSuchElementException.class, () -> {
            histoService.getHistoriqueByPatientId("999");
        });

        verify(histoRepository).existsByPatId(999L);
        verify(histoRepository, never()).findByPatId(anyLong());
    }
}
