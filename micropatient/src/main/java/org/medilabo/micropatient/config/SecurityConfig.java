package org.medilabo.micropatient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * To manage the security
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> {
                    csrf.disable();
                })
                .cors(Customizer.withDefaults())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // Autoriser le jeton anonyme à accéder aux méthodes GET, POST, PUT sur /api/patients
                    auth.requestMatchers(HttpMethod.GET, "/patients/**").hasAuthority("ROLE_ANONYMOUS");
                    auth.requestMatchers(HttpMethod.POST, "/patients").hasAuthority("ROLE_ANONYMOUS");
                    auth.requestMatchers(HttpMethod.PUT, "/patients/**").hasAuthority("ROLE_ANONYMOUS");

                    // Autoriser l'accès non authentifié à cet endpoint qui fournit le token anonyme
                    auth.requestMatchers("/auth/anonymous").permitAll();

                    // Toute autre requête nécessite une authentification
                    auth.anyRequest().authenticated();
                })
                .build();
    }

}
