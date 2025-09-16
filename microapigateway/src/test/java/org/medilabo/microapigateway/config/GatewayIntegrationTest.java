package org.medilabo.microapigateway.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.medilabo.microapigateway.web.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class GatewayIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    void setup() {
        when(jwtService.isTokenValid("valid-token")).thenReturn(true);
        when(jwtService.extractUsername("valid-token")).thenReturn("anonymous");

        when(jwtService.isTokenValid("invalid-token")).thenReturn(false);
    }

    /**
     *
     * Should return unauthorized without token
     * Appel d’une route protégée sans token donc doit renvoyer 401
     * car le filtre JWT le rejette
     *
     */
    @Test
    void shouldReturnUnauthorizedWithoutToken() {
        //Act and Assert
        webTestClient.get()
                .uri("/patients")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    /**
     *
     * Should return unauthorized with invalid token
     * Appel d’une route protégée avec token invalide  donc doit renvoyer  401
     * car le service JWT le rejette
     *
     */
    @Test
    void shouldReturnUnauthorizedWithInvalidToken() {
        //Arrange, Act and Assert
        webTestClient.get()
                .uri("/patients")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    /**
     *
     * Should allow request with valid token
     * Appel d’une route protégée avec token valide donc passe le filtre
     * mais renvoie 5xx car pas de backend
     *
     */
    @Test
    void shouldAllowRequestWithValidToken() {
        //Arrange, Act and Assert
        webTestClient.get()
                .uri("/patients")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid-token")
                .exchange()
                .expectStatus().is5xxServerError(); // Car pas de vrai backend, mais on est passé par le filtre
    }

    /**
     *
     * Should allow public route without token
     * Appel d’une route autorisée sans token donc le service JWT est ignoré
     * mais renvoie 5xx car pas de backend
     *
     */
    @Test
    void shouldAllowPublicRouteWithoutToken() {
        //Act and Assert
        webTestClient.get()
                .uri("/auth/anonymous")
                .exchange()
                .expectStatus().is5xxServerError();
    }

}
