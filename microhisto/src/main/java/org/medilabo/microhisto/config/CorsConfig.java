package org.medilabo.microhisto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 *
 * Mécanisme de sécurité des navigateurs qui empêche par défaut un frontend
 * (ex: Angular sur localhost:4200) de faire des requêtes vers un backend
 * hébergé sur un autre domaine
 *
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost", "http://localhost:8080", "http://localhost:4200",
                "http://localhost:5900", "http://localhost:8996"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*")); // permet à tous les headers d'être envoyés dans les requêtes (ex: Authorization, Content-Type, etc.).
        config.setAllowCredentials(true); // permet l’envoi de cookies, tokens JWT, ou headers d’authentification avec les requêtes CORS

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;

    }
}
