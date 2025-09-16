package org.medilabo.microrisque.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.medilabo.microrisque.web.controller.RisqueController;
import org.medilabo.microrisque.web.service.JwtValidationService;
import org.medilabo.microrisque.web.service.RisqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(RisqueController.class)
@Import(RisqueControllerMockMvcTest.TestSecurityConfig.class)
public class RisqueControllerMockMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RisqueService risqueService;

    @MockBean
    private JwtValidationService jwtValidationService;

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

            return http
                    .csrf(csrf -> {
                        csrf.disable();
                    })
                    .cors(cors -> cors.disable())
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> {
                        auth.anyRequest().permitAll(); // aucune restriction

                    })
                    .build();
        }
    }

    @BeforeEach
    void setupSecurityContext() {
        // Simuler un utilisateur
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("anonymous");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    /**
     *
     * Should return 200 when the service succeeds
     *
     */
    @Test
    @WithMockUser(username = "anonymous")
    void shouldReturn200WhenTheServiceSucceeds() throws Exception {
        //Arrange
        String patientId = "12345";
        String expectedResponse = "Aucun risque (None)";

        when(risqueService.evaluerRisque(patientId)).thenReturn(expectedResponse);

        //Act and Assert
        mockMvc.perform(get("/evaluer/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(risqueService, times(1)).evaluerRisque(patientId);
    }

    /**
     *
     * Should return 500 when the service fails
     *
     */
    @Test
    @WithMockUser(username = "anonymous")
    void shouldReturn500WhenTheServiceFails() throws Exception {
        //Arrange
        String patientId = "12345";
        when(risqueService.evaluerRisque(patientId)).thenThrow(new RuntimeException("Service indisponible"));

        //Act and Assert
        mockMvc.perform(get("/evaluer/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Erreur lors de l'évaluation du risque")));

        verify(risqueService, times(1)).evaluerRisque(patientId);
    }
}
