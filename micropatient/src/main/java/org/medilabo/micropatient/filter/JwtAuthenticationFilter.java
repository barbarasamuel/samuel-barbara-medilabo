package org.medilabo.micropatient.filter;

import org.medilabo.micropatient.web.service.JwtValidationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtValidationService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        log.info(">>> JwtAuthenticationFilter appelé");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Authorization header absent ou mal formé");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.isTokenValid(token)) {
                log.debug("Token JWT valide pour l'utilisateur {}", username);

                // Extraire les rôles depuis le token
                List<String> roles = jwtService.extractAuthorities(token); //  à implémenter
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.warn("Token JWT invalide ou expiré");
            }
        }

        filterChain.doFilter(request, response);
    }

/*@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtValidationService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        log.info(">>> JwtAuthenticationFilter appelé");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Authorization header absent ou mal formé");
            filterChain.doFilter(request, response);
            return;
        }
        log.debug("JwtAuthenticationFilter exécuté - vérification du token : {}", authHeader);
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.isTokenValid(token)) {//, username)) {
                log.debug("Token JWT valide pour l'utilisateur {}", username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        //username, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        username, null, List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }else {
                log.warn("Token JWT invalide ou expiré");
            }
        }

        filterChain.doFilter(request, response);
    }
    */
}