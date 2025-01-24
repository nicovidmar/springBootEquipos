package com.futbol.equipos.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Key key;

    /**
     * Constructor que inicializa la clave de firma JWT utilizando una clave secreta.
     * 
     * @param secretKey La clave secreta configurada en las propiedades (security.jwt.secret-key).
     * @throws RuntimeException Si la clave secreta no está configurada o es inválida.
     */
    public JwtUtil(@Value("${security.jwt.secret-key}") String secretKey) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new RuntimeException("La clave secreta para JWT no está configurada");
        }
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Extrae el nombre de usuario (subject) de un token JWT.
     * 
     * @param token El token JWT del cual extraer el nombre de usuario.
     * @return El nombre de usuario contenido en el token.
     */
    public String extractUsername(String token) {
        return getParser().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Genera un token JWT para un usuario dado.
     * 
     * @param username El nombre de usuario para el cual se generará el token.
     * @return El token JWT generado.
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Crea un token JWT con los datos proporcionados.
     * 
     * @param claims Un mapa de claims personalizados que se incluirán en el token.
     * @param subject El nombre de usuario o sujeto asociado con el token.
     * @return El token JWT generado.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(key)
                .compact();
    }

    /**
     * Obtiene un parser configurado para analizar y validar tokens JWT.
     * 
     * @return Un objeto JwtParser configurado con la clave de firma.
     */
    public JwtParser getParser() {
        return Jwts.parserBuilder()
                .setSigningKey(key) 
                .build(); 
    }

    /**
     * Valida un token JWT asegurándose de que sea válido y no haya expirado.
     * 
     * @param token El token JWT a validar.
     * @return `true` si el token es válido, `false` si es inválido o expirado.
     */
    public boolean validateToken(String token) {
        try {
            getParser().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
