package org.medilabo.microhisto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.medilabo.microhisto.dto.HistoriqueDTO;
import org.medilabo.microhisto.web.service.JwtValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.medilabo.microhisto.model.Histo;
import org.medilabo.microhisto.web.service.HistoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.medilabo.microhisto.web.controller.HistoController;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistoController.class)
@Import(HistoControllerMockMvcTest.TestSecurityConfig.class)
public class HistoControllerMockMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtValidationService jwtValidationService;

    @MockBean
    private HistoService histoService;

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

            return http
                    .csrf(csrf -> {
                        csrf.disable();
                    })
                    .cors(Customizer.withDefaults())
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> {
                        auth.anyRequest().permitAll();

                    })
                    .build();
        }
    }

    @BeforeEach
    void setupAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("anonymous", "", List.of())
        );
    }

    /**
     *
     * Should return all the patient histos
     *
     */
    @Test
    @WithMockUser(username = "anonymous")
    void shouldReturnAllHistosByPatientTest() throws Exception {
        //Arrange
        String patientId = "100";

        Histo histo1 = new Histo();
        histo1.setId("1");
        histo1.setPatId(100L);
        histo1.setNote("S'est mis à fumer");

        Histo histo2 = new Histo();
        histo2.setId("2");
        histo2.setPatId(100L);
        histo2.setNote("Est devenu fumeur");

        List<Histo> histos = List.of(histo1, histo2);

        when(histoService.getHistoByPatient(100L)).thenReturn(histos);

        //Act
        mockMvc.perform(get("/hist/" + patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    /**
     *
     * Should return the note by id
     *
     */
    @Test
    @WithMockUser(username = "anonymous")
    void shouldReturnNoteByIdTest() throws Exception {
        //Arrange
        String histoId = "12345";
        Histo histo = new Histo();
        histo.setId(histoId);
        histo.setPatient("Dupont");
        histo.setNote("S'est mis à fumer");

        when(histoService.getNoteById(histoId)).thenReturn(histo);

        //Act
        mockMvc.perform(get("/hist/details/" + histoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(histoId))
                .andExpect(jsonPath("$.note").value(histo.getNote()));
    }

    /**
     *
     * Should insert a histo
     *
     */
    @Test
    @WithMockUser(username = "anonymous")
    void shouldInsertAHistoTest() throws Exception {
        //Arrange
        Histo inputHisto = new Histo();
        inputHisto.setPatId(100L);
        inputHisto.setPatient("Dupont");
        inputHisto.setNote("S'est mis à fumer");

        Histo savedHisto = new Histo();
        savedHisto.setId("12345");
        savedHisto.setPatId(100L);
        savedHisto.setPatient("Dupont");
        savedHisto.setNote("S'est mis à fumer");

        when(histoService.insert(any(Histo.class))).thenReturn(savedHisto);

        //Act
        mockMvc.perform(post("/hist/creation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(inputHisto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patient").value("Dupont"))
                .andExpect(jsonPath("$.note").value("S'est mis à fumer"));
    }

    /**
     *
     * Should return patient historique
     *
     */
    @Test
    @WithMockUser(username = "anonymous")
    void shouldReturnHistoriqueDTO() throws Exception {
        //Arrange
        String patientId = "420";
        List<String> notesList = new ArrayList<>();
        notesList.add("S'est mis à fumer");
        notesList.add("Continue à fumer");

        HistoriqueDTO dtoHisto = new HistoriqueDTO();
        dtoHisto.setPatId(420L);
        dtoHisto.setNote(notesList);

        when(histoService.getHistoriqueByPatientId(patientId)).thenReturn(dtoHisto);

        //Act
        mockMvc.perform(get("/hist/risque/" + patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note[0]").value("S'est mis à fumer"))
                .andExpect(jsonPath("$.note[1]").value("Continue à fumer"));
    }

    /**
     *
     * Should return not found
     *
     */
    @Test
    @WithMockUser(username = "anonymous")
    void shouldReturnNotFoundIfPatientNotExist() throws Exception {
        //Arrange
        String patientId = "12345";

        when(histoService.getHistoriqueByPatientId(patientId)).thenThrow(new RuntimeException("Not found"));

        //Act
        mockMvc.perform(get("/hist/risque/" + patientId))
                .andExpect(status().isNotFound());
    }
}
