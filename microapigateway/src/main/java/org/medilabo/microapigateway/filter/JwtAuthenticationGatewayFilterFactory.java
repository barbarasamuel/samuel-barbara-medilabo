package org.medilabo.microapigateway.filter;

import org.medilabo.microapigateway.web.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {

    private final JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationGatewayFilterFactory.class);
    private static final String BEARER_PREFIX = "Bearer ";

    // Liste blanche des chemins à ignorer
    //private static final Set<String> EXCLUDE_PATHS = Set.of("/auth/anonymous");
    public JwtAuthenticationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ///////////////////////
            // Exclusions
            String path = exchange.getRequest().getURI().getPath();
            if (path.equals("/") || path.contains("/auth/anonymous")) {//if (path.equals("/auth/anonymous")) {
                return chain.filter(exchange);
            }/**/
            //////////////////////
// Bypass pour les endpoints anonymes

            /*if (EXCLUDE_PATHS.contains(path)) {
                return chain.filter(exchange);
            }*/
            // Sinon, logique JWT classique
            List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);
            if (authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer ")) {
                return unauthorized(exchange);
            }

            String token = authHeaders.get(0).substring(7); // Remove "Bearer "
            if (!jwtService.isTokenValid(token)) {
                return unauthorized(exchange);
            }
            ///////////////////
            if (!config.isSecured()) {
                log.debug("Route non sécurisée, passage sans vérification JWT");
                return chain.filter(exchange);
            }

            return authenticateRequest(exchange, chain);
        };
    }

    ////////////////////////
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
    ///////////////////////
    private Mono<Void> authenticateRequest(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // Vérification du header Authorization
        if (isInvalidAuthHeader(authHeader)) {
            log.warn("Header Authorization manquant ou invalide pour la requête : {}", request.getPath());
            return handleUnauthorized(exchange, "Token manquant ou format invalide");
        }

        String token = extractToken(authHeader);

        try {
            return validateAndProcessToken(exchange, chain, token);
        } catch (ExpiredJwtException e) {
            log.warn("Token expiré : {}", e.getMessage());
            return handleUnauthorized(exchange, "Token expiré");
        } catch (JwtException e) {
            log.warn("Token JWT invalide : {}", e.getMessage());
            return handleUnauthorized(exchange, "Token invalide");
        } catch (Exception e) {
            log.error("Erreur lors de la validation du token : {}", e.getMessage(), e);
            return handleUnauthorized(exchange, "Erreur d'authentification");
        }
    }

    private boolean isInvalidAuthHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith(BEARER_PREFIX) || authHeader.length() <= BEARER_PREFIX.length();
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(BEARER_PREFIX.length());
    }

    private Mono<Void> validateAndProcessToken(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        if (!jwtService.isTokenValid(token)) {
            log.warn("Token invalide détecté");
            return handleUnauthorized(exchange, "Token invalide");
        }

        String username = jwtService.extractUsername(token);
        log.debug("Token valide pour l'utilisateur : {}", username);

        // Enrichissement de la requête avec les informations d'authentification
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header("X-User-Name", username)
                .header("X-Auth-Token", token)
                .header("X-Authenticated", "true")
                .build();

        ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
        return chain.filter(modifiedExchange);
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String reason) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        // Ajout d'un header informatif (optionnel)
        response.getHeaders().add("WWW-Authenticate", "Bearer");

        // Optionnel : ajouter un body JSON avec le message d'erreur
        if (shouldIncludeErrorBody()) {
            String errorBody = String.format("{\"error\":\"Unauthorized\",\"message\":\"%s\"}", reason);
            DataBuffer buffer = response.bufferFactory().wrap(errorBody.getBytes(StandardCharsets.UTF_8));
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            return response.writeWith(Mono.just(buffer));
        }

        return response.setComplete();
    }

    private boolean shouldIncludeErrorBody() {
        // Vous pouvez rendre cela configurable
        return true;
    }

    public static class Config {
        private boolean secured = true;
        private boolean includeUserInfo = true;

        public boolean isSecured() {
            return secured;
        }

        public void setSecured(boolean secured) {
            this.secured = secured;
        }

        public boolean isIncludeUserInfo() {
            return includeUserInfo;
        }

        public void setIncludeUserInfo(boolean includeUserInfo) {
            this.includeUserInfo = includeUserInfo;
        }
    }
    /*@Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationGatewayFilterFactory.class);

    public JwtAuthenticationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        log.debug("JwtAuthenticationGatewayFilterFactory exécuté - vérification du token", config);

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!config.isSecured()) {
                // Pas besoin de token, on laisse passer la requête
                return chain.filter(exchange);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            try {
                if (!jwtService.isTokenValid(token)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String username = jwtService.extractUsername(token);
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-User-Name", username)
                        .header("X-Auth-Token", token)
                        .build();

                ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
                return chain.filter(modifiedExchange);

            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    public static class Config {
        private boolean secured = true;

        public boolean isSecured() {
            return secured;
        }

        public void setSecured(boolean secured) {
            this.secured = secured;
        }
    }*/
}