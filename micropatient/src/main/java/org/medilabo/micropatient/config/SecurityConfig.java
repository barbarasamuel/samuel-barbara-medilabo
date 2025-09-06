package org.medilabo.micropatient.config;

import org.medilabo.micropatient.filter.JwtAuthenticationFilter;
import org.medilabo.micropatient.config.CorsConfig;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 *
 * To manage the security
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private CorsConfig corsConf;
/*
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> {
                    csrf.disable();
                })
                .cors(Customizer.withDefaults())
                //.cors(cors -> cors.disable())
                //.cors(cors -> cors.configurationSource(corsConf.corsConfigurationSource()))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    //auth.anyRequest().permitAll(); // aucune restriction ici
                    //avtdernier auth.anyRequest().authenticated();
                    // Autoriser le jeton anonyme à accéder aux méthodes GET, POST, PUT sur /api/patients
                    auth.requestMatchers(HttpMethod.GET, "/patients/**").hasAuthority("ROLE_ANONYMOUS");
                    auth.requestMatchers(HttpMethod.POST, "/patients").hasAuthority("ROLE_ANONYMOUS");
                    auth.requestMatchers(HttpMethod.PUT, "/patients/**").hasAuthority("ROLE_ANONYMOUS");

                    // Autoriser l'accès non authentifié au endpoint qui fournit le token anonyme
                    auth.requestMatchers("/auth/anonymous").permitAll();

                    // Toute autre requête nécessite une authentification
                    auth.anyRequest().authenticated();
                    //auth.requestMatchers(HttpMethod.POST, "/api/patients").hasAuthority("ROLE_ANONYMOUS");
                    /*auth.requestMatchers("/patients/**").authenticated(); // accès avec JWT requis
                    auth.anyRequest().denyAll();*/
                    /*auth.requestMatchers("/actuator/health").permitAll();
                    auth.anyRequest().authenticated();*/
                    //auth.anyRequest().permitAll();
                })
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)//;
                .build();
    }

}
