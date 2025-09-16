package org.medilabo.microhisto.service;

import org.junit.jupiter.api.Test;
import org.medilabo.microhisto.dto.HistoriqueDTO;
import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.dao.HistoRepository;
import org.medilabo.microhisto.web.service.HistoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class HistoServiceIntegrationTest {
    @Autowired
    private HistoService histoService;

    @Autowired
    private HistoRepository histoRepository;

    /**
     *
     * Should insert and get histo by patient
     *
     */
    @Test
    public void shouldInsertAndGetHistoByPatientTest() {
        //Arrange
        Histo histo = new Histo();
        histo.setPatId(200L);
        histo.setPatient("Martin");
        histo.setNote("En bonne santé");

        //Act
        histoService.insert(histo);

        //Assert
        List<Histo> result = histoService.getHistoByPatient(200L);
        assertEquals(1, result.size());
        assertEquals("En bonne santé", result.get(0).getNote());

        histoRepository.deleteByPatId(200L);
        List<Histo> histoList = histoRepository.findByPatId(200L);
        assertEquals(0, histoList.size());
    }

    /**
     *
     * Should  get a note by id
     *
     */
    @Test
    public void shouldGetNoteByIdTest() {
        //Arrange
        Histo histo = new Histo();
        histo.setPatId(201L);
        histo.setPatient("Dupond");
        histo.setNote("Hémoglobine A1C");

        Histo saved = histoService.insert(histo);

        //Act
        Histo found = histoService.getNoteById(saved.getId());

        //Assert
        assertNotNull(found);
        assertEquals("Hémoglobine A1C", found.getNote());

        histoRepository.deleteByPatId(201L);
        List<Histo> histoList = histoRepository.findByPatId(202L);
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
        Histo h1 = new Histo();
        h1.setPatId(202L);
        h1.setPatient("PatientTest");
        h1.setNote("Première réaction");

        Histo h2 = new Histo();
        h2.setPatId(202L);
        h2.setPatient("PatientTest");
        h2.setNote("Deuxième réaction");

        histoService.insert(h1);
        histoService.insert(h2);

        //Act
        HistoriqueDTO dto = histoService.getHistoriqueByPatientId("202");

        //Assert
        assertEquals(202L, dto.getPatId());
        assertEquals(2, dto.getNote().size());
        assertTrue(dto.getNote().contains("Première réaction"));
        assertTrue(dto.getNote().contains("Deuxième réaction"));

        histoRepository.deleteByPatId(202L);
        List<Histo> histoList = histoRepository.findByPatId(202L);
        assertEquals(0, histoList.size());
    }

    /**
     *
     * Should not find historique by patientId
     *
     */
    @Test
    public void shouldNotFindHistoriqueByPatientIdTest() {
        //Arrange: noting in the database for this patientId

        //Act and assert
        assertThrows(NoSuchElementException.class, () -> {
            histoService.getHistoriqueByPatientId("9999");
        });
    }
}
