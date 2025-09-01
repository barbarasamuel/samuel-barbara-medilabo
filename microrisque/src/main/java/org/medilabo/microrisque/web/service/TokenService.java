package org.medilabo.microrisque.web.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String secret;

    //private static final String SECRET_KEY = "supersecretencryptionkeythatshouldbelongenough1234";
/////////////////////////////////
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
/////////////////////////////////
    /*////////////////private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }*/

    // Extrait le nom d'utilisateur du token JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Utilitaire générique pour extraire n'importe quelle info du token
    /*public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }*/
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()  // parser() au lieu de parserBuilder()
                .verifyWith(getSigningKey())  // verifyWith() au lieu de setSigningKey()
                .build()
                .parseSignedClaims(token)  // parseSignedClaims() au lieu de parseClaimsJws()
                .getPayload();  // getPayload() au lieu de getBody()
        return claimsResolver.apply(claims);
    }

    // Vérifie que le token est bien pour cet utilisateur et n'est pas expiré
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Crée un JWT (si besoin d’en générer un)
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10h
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


}
