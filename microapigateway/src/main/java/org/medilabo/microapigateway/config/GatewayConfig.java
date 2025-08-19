package org.medilabo.microapigateway.config;

import org.medilabo.microapigateway.filter.JwtAuthenticationFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * Gateway configuration
 *
 */
@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilterFactory jwtFilter;

    public GatewayConfig(JwtAuthenticationFilterFactory jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route pour les microservices protégés
                .route("micropatient", r -> r
                        .path("/patients/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilterFactory.Config())))
                        .uri("http://localhost:8999"))//.uri("http://micropatient:8999"))//lb://MICROPATIENT

                .route("microhisto", r -> r
                        .path("/hist/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilterFactory.Config())))
                        .uri("http://localhost:8998"))//.uri("http://microhisto:8998"))////lb://MICROHISTO

                .route("microrisque", r -> r
                        .path("/evaluer/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilterFactory.Config())))
                        .uri("http://localhost:8997"))

                // Routes publiques (sans JWT)
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("http://localhost:8996"))

                .build();
    }
}
