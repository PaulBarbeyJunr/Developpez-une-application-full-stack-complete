package com.openclassrooms.mddapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utilitaire de gestion des JSON Web Tokens (JWT) pour l'authentification.
 * Fournit la génération et la validation des tokens utilisés par
 * {@link JwtAuthFilter} pour authentifier les requêtes entrantes.
 */
@Component
@Slf4j
public class JwtUtils {

    @Value("${mdd.app.jwtSecret}")
    private String jwtSecret;

    @Value("${mdd.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Génère un JWT signé pour l'utilisateur authentifié.
     *
     * @param userDetails les détails de l'utilisateur (issus de Spring Security)
     * @return le token JWT compact (chaîne signée)
     */
    public String generateJwtToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrait l'identifiant (subject) du token. Pour ce projet, le subject
     * contient le nom d'utilisateur ({@code username}).
     *
     * @param token le JWT à analyser
     * @return le nom d'utilisateur contenu dans le token
     */
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valide la signature et la date d'expiration d'un token JWT.
     *
     * @param authToken le token à valider
     * @return {@code true} si le token est valide, {@code false} sinon
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("JWT mal formé : {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT expiré : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT non supporté : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Chaîne JWT vide : {}", e.getMessage());
        }
        return false;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(io.jsonwebtoken.io.Encoders.BASE64.encode(jwtSecret.getBytes()));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
