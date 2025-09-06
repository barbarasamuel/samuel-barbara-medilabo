package org.medilabo.microapigateway.web.service;

import io.jsonwebtoken.Jwts;
/////////////////////
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    //@Value("${jwt.secret:mySecretKey1234567890123456789012345678901234567890}")
    @Value("${jwt.secret:myVeryLongSecretKeyForJWT1234567890123456789012345678901234567890}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24 heures en millisecondes
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration:604800000}") // 7 jours
    private Long refreshExpiration;

    private final MacAlgorithm algorithm = Jwts.SIG.HS512;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
///////////////////////////////////////////////////
    public String generateToken(String username, List<String> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 1000 * 60 * 60 * 24); // 24h

        return Jwts.builder()
                .subject(username)
                .claim("authorities", roles)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey(), algorithm)
                .compact();
    }
    ////////////////////////////////////////////////////
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    public String generateToken(Map<String, Object> extraClaims, String username) {
        return buildToken(extraClaims, username, jwtExpiration);
    }

    public String generateRefreshToken(String username) {
        return buildToken(new HashMap<>(), username, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            String username,
            long expiration
    ) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}