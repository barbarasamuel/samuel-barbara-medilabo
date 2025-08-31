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
                        username, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }else {
                log.warn("Token JWT invalide ou expiré");
            }
        }

        filterChain.doFilter(request, response);
    }
    /*@Autowired
    private JwtValidationService jwtService;

    public JwtAuthenticationFilter(JwtValidationService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Vérifier si l'utilisateur est déjà passé par la Gateway (header X-User-Name)
        String username = request.getHeader("X-User-Name");
        String token = request.getHeader("X-Auth-Token");

        if (username != null && token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Valider le token reçu de la Gateway
            if (jwtService.isTokenValid(token)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        new ArrayList<>() // Ajout des rôles ici
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }*/
}