package org.medilabo.microapigateway.configwithBackend;

import org.junit.jupiter.api.Test;
import org.medilabo.microapigateway.web.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

/*@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import({GatewayTestConfig.class, MockBackendController.class})
public class GatewayBackendIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private JwtService jwtService;
*/
    /**
     *
     * Should allow request with valid token
     * Appel d’une route protégée avec token valide donc passe le filtre
     * et accès au backend ok
     *
     */
 /*   @Test
    void shouldAllowRequestWithValidTokenAndBackend() {
        //Arrange, Act and Assert
        when(jwtService.isTokenValid("valid-token")).thenReturn(true);
        when(jwtService.extractUsername("valid-token")).thenReturn("anonymous");

        webTestClient.get()
                .uri("/patients")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid-token")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Mocked patient response");
    }
}*/
