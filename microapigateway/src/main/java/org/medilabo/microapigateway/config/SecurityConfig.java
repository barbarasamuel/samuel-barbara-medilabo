package org.medilabo.microapigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;

/**
 *
 * To manage the security
 *
 */
@Configuration
@EnableWebFluxSecurity//@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /////////////////////////////////////////////

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return username -> {
            if ("admin".equals(username)) {
                UserDetails user = User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("password"))
                        .roles("USER", "ADMIN")
                        .build();
                return Mono.just(user);
            } else {
                return Mono.empty();
            }
        };
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }
    /////////////////////////////////////////////


    //@Bean
    //public SecurityWebFilterChain securityWebFilterChain(HttpSecurity http) throws Exception{
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)//////////
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                )///////////////////////
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                /*.authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/actuator/health").permitAll()
                        .anyExchange().authenticated()


                        .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/histo/**", "/patient/**", "/evaluer/**").authenticated()
                .anyRequest().denyAll()*/
                /*.authorizeExchange(exchange -> exchange
                        //.anyExchange().permitAll()
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/histo/**", "/patient/**", "/evaluer/**").authenticated()
                        .anyExchange().denyAll()
                )*/
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/", "/auth/anonymous").permitAll()
                        .anyExchange().permitAll()) // Pas de blocage ici, filtre JWT s'en charge
                .build();
    }

}
