package org.medilabo.microapigateway.controller;

import org.junit.jupiter.api.Test;
import org.medilabo.microapigateway.web.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ReactiveAuthenticationManager authenticationManager;

    /**
     *
     * Should get a token
     *
     */
    @Test
    void shouldReturnAnonymousTokens() {
        //Arrange
        String dummyAccessToken = "dummy-access-token";
        String dummyRefreshToken = "dummy-refresh-token";

        when(jwtService.generateToken("anonymous", List.of("ROLE_ANONYMOUS"))).thenReturn(dummyAccessToken);
        when(jwtService.generateRefreshToken("anonymous")).thenReturn(dummyRefreshToken);

        //Act and Assert
        webTestClient.get()
                .uri("/auth/anonymous")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accessToken").isEqualTo(dummyAccessToken)
                .jsonPath("$.refreshToken").isEqualTo(dummyRefreshToken)
                .jsonPath("$.username").isEqualTo("anonymous")
                .jsonPath("$.authorities[0]").isEqualTo("ROLE_ANONYMOUS");
    }
}
