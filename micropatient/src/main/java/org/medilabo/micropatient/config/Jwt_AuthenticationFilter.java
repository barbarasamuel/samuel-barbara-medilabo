package org.medilabo.micropatient.config;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.medilabo.micropatient.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class Jwt_AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, java.io.IOException {

        final String authHeader = request.getHeader("Authorization");
        
        // Log the request path for debugging
        System.out.println("JwtAuthenticationFilter processing request to path: " + request.getRequestURI());

        // Always allow access to /patients/** endpoints without authentication
        if (request.getRequestURI().startsWith("/patients")) {
            System.out.println("Allowing access to /patients endpoint without authentication");
            filterChain.doFilter(request, response);
            return;
        }

        // Vérifie que le header Authorization est présent et bien formaté
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Authorization header or not Bearer token, passing to next filter");
            filterChain.doFilter(request, response); // laisse passer sans auth
            return;
        }

        final String jwt = authHeader.substring(7); // extrait le token
        final String username = tokenService.extractUsername(jwt);

        // Si l’utilisateur est déjà authentifié, on ne fait rien
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (tokenService.isTokenValid(jwt, userDetails)) {
                // Crée un token d’auth Spring Security, sans mot de passe
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Injecte dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response); // continue la chaîne
    }

}
