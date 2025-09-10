package org.medilabo.microapigateway.web.controller;

import org.medilabo.microapigateway.dto.AuthRequest;
import org.medilabo.microapigateway.dto.AuthResponse;
import org.medilabo.microapigateway.dto.RefreshTokenRequest;
import org.medilabo.microapigateway.web.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, ReactiveAuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    /**
     *
     * To get the token
     *
     */
    @GetMapping("/anonymous")
    public Mono<ResponseEntity<?>> anonymous() {

        String accessToken = jwtService.generateToken("anonymous", List.of("ROLE_ANONYMOUS"));
        String refreshToken = jwtService.generateRefreshToken("anonymous");

        return Mono.just(ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "username", "anonymous",
                "authorities", List.of("ROLE_ANONYMOUS")
        )));

    }

}
