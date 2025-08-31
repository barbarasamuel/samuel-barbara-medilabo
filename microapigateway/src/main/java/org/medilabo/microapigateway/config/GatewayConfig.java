package org.medilabo.microapigateway.config;

import org.medilabo.microapigateway.filter.JwtAuthenticationGatewayFilterFactory;
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
    private JwtAuthenticationGatewayFilterFactory jwtFilter;

    public GatewayConfig(JwtAuthenticationGatewayFilterFactory jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route pour les microservices protégés
                .route("micropatient", r -> r
                        .path("/patients/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationGatewayFilterFactory.Config())))
                        .uri("lb://MICROPATIENT"))//.uri("http://localhost:8999"))//

                .route("microhisto", r -> r
                        .path("/hist/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationGatewayFilterFactory.Config())))
                        .uri("lb://MICROHISTO"))//.uri("http://localhost:8998"))////

                .route("microrisque", r -> r
                        .path("/evaluer/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationGatewayFilterFactory.Config())))
                        .uri("lb://MICRORISQUE"))//.uri("http://localhost:8997"))

                // Routes publiques (sans JWT)
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("lb://MICROAPIGATEWAY"))//.uri("http://localhost:8996"))

                .route("home", r -> r.path("/")
                        .uri("http://localhost:5900"))
                .build();
    }
}
