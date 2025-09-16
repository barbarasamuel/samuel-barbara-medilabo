package org.medilabo.microapigateway.configwithBackend;

import org.medilabo.microapigateway.filter.JwtAuthenticationGatewayFilterFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/*@TestConfiguration
public class GatewayTestConfig {
    @Bean
    public RouteLocator testRoutes(RouteLocatorBuilder builder, JwtAuthenticationGatewayFilterFactory jwtFilter) {
        return builder.routes()
                .route("mocked-patient", r -> r
                        .path("/patients")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationGatewayFilterFactory.Config())))
                        .uri("http://localhost:9999")) // faux backend
                .build();
    }
}*/
