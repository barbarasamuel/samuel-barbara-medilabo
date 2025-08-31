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

    /*@PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest request) {
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        return authenticationManager.authenticate(authRequest)
                .map(authentication -> {
                    // Authentification réussie
                    String accessToken = jwtService.generateToken(request.getUsername());
                    String refreshToken = jwtService.generateRefreshToken(request.getUsername());

                    AuthResponse response = new AuthResponse();
                    response.setUsername(request.getUsername());
                    response.setAccessToken(accessToken);
                    response.setRefreshToken(refreshToken);

                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<AuthResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        try {
            String username = jwtService.extractUsername(request.getRefreshToken());

            if (jwtService.isTokenValid(request.getRefreshToken(), username)) {
                String accessToken = jwtService.generateToken(username);

                AuthResponse response = new AuthResponse();
                response.setAccessToken(accessToken);
                response.setRefreshToken(request.getRefreshToken());
                response.setUsername(username);

                return Mono.just(ResponseEntity.ok(response));
            }

            return Mono.just(ResponseEntity.badRequest().build());

        } catch (Exception e) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
    }

    @PostMapping("/validate")
    public Mono<ResponseEntity<Boolean>> validateToken(@RequestParam String token) {
        boolean isValid = jwtService.isTokenValid(token);
        return Mono.just(ResponseEntity.ok(isValid));
    }*/

    @GetMapping("/anonymous")
    public Mono<ResponseEntity<AuthResponse>> anonymous() {
        String username = "anonymous";
        String accessToken = jwtService.generateToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);

        AuthResponse response = new AuthResponse();
        response.setUsername(username);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        //response.setRefreshToken(null); // pas de refresh pour anonymes

        return Mono.just(ResponseEntity.ok(response));
    }
    /*@PostMapping("/anonymous")
    public Mono<ResponseEntity<AuthResponse>> anonymous() {
        String username = "anonymous";
        String accessToken = jwtService.generateToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);

        AuthResponse response = new AuthResponse();
        response.setUsername(username);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        //response.setRefreshToken(null); // pas de refresh pour anonymes

        return Mono.just(ResponseEntity.ok(response));
    }*/
}
