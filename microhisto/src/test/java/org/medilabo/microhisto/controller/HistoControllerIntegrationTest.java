package org.medilabo.microhisto.controller;

import org.junit.jupiter.api.Test;
import org.medilabo.microhisto.dto.HistoriqueDTO;
import org.medilabo.microhisto.model.Histo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HistoControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/hist";
    }

    /**
     *
     * Should get histo list of a patient
     *
     */
    @Test
    void shouldReturnHistoList() {
        //Act
        ResponseEntity<Histo[]> response = restTemplate.getForEntity(getBaseUrl() + "/1", Histo[].class);

        //Assert
        assertEquals(HttpStatus.OK,response.getStatusCode());

    }

    /**
     *
     * Should create a histo
     *
     */
    @Test
    void shouldCreateHisto() {
        //Arrange
        Histo histo = new Histo();
        histo.setPatId(100L);
        histo.setPatient("Dupont");
        histo.setNote("S'est mis à fumer");

        //Act
        ResponseEntity<Histo> response = restTemplate.postForEntity(getBaseUrl() + "/creation", histo, Histo.class);

        //Assert
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("S'est mis à fumer",response.getBody().getNote());

    }

    /**
     *
     * Should return a histo
     *
     */
    @Test
    void shouldReturnAHisto() {
        //Arrange
        String histoId = "1";

        //Act
        ResponseEntity<Histo> response = restTemplate.getForEntity(getBaseUrl() + "/details/" + histoId, Histo.class);

        //Assert
        assertEquals(HttpStatus.OK,response.getStatusCode());

    }

    /**
     *
     * Should get a HistoriqueDTO
     *
     */
    @Test
    void shouldReturnHistoriqueDTO() {
        //Arrange
        String patientId = "1";

        //Act
        ResponseEntity<HistoriqueDTO> response = restTemplate.getForEntity(getBaseUrl() + "/risque/" + patientId, HistoriqueDTO.class);

        //Assert
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());

    }

    /**
     *
     * Should return 404 if not found
     *
     */
    @Test
    void shouldReturnNotFound_whenPatientNotExists() {
        //Arrange
        String patientId = "9999";

        //Act
        ResponseEntity<HistoriqueDTO> response = restTemplate.getForEntity(getBaseUrl() + "/risque/" + patientId, HistoriqueDTO.class);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
}
